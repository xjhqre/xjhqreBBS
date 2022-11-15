import os
import socket
import threading
from io import BytesIO

import numpy as np
import oss2
import requests
import tensorflow as tf
from PIL import Image
from elasticsearch import Elasticsearch
from keras_applications.resnet50 import ResNet50
from numpy import linalg as LA
from tensorflow.keras.applications.resnet50 import preprocess_input
from tensorflow.keras.preprocessing import image
from tensorflow.python import keras
from tensorflow.python.keras.backend import set_session

import config

'''
    提取图片特征向量上传阿里云OSS
'''

# 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
auth = oss2.Auth(config.AccessKeyId, config.AccessKeySecret)
# yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
# 填写Bucket名称。
bucket = oss2.Bucket(auth, config.EndPoint, config.bucket)


# 上传文件到指定bucket
def upload_2_oss(file_name, file_path):
    # 例  file_name：test/imageName.jpg
    return bucket.put_object_from_file(file_name, file_path)


class FeatureExtractor:
    def __init__(self):
        # Milvus
        self.session = tf.compat.v1.Session()
        set_session(self.session)
        self.graph = tf.compat.v1.get_default_graph()
        self.model = ResNet50(
            weights='imagenet',
            include_top=False,
            pooling='avg',
            backend=keras.backend,
            layers=keras.layers,
            models=keras.models,
            utils=keras.utils
        )

    def execute(self, img_path):
        img = image.load_img(img_path, target_size=(224, 224))
        x = image.img_to_array(img)
        x = np.expand_dims(x, axis=0)
        x = preprocess_input(x)
        with self.graph.as_default():
            with self.session.as_default():
                features = self.model.predict(x)
                norm_feature = features[0] / LA.norm(features[0])
                norm_feature = [i.item() for i in norm_feature]
                return norm_feature[::2]


class ServerThreading(threading.Thread):
    def __init__(self, clientsocket, recvsize=1024 * 1024, encoding="utf-8"):
        threading.Thread.__init__(self)
        self._socket = clientsocket
        self._recvsize = recvsize
        self._encoding = encoding
        pass

    def run(self):
        print("开启线程.....")
        try:
            # 接受数据
            msg = ''
            while True:
                # 读取recvsize个字节
                rec = self._socket.recv(self._recvsize)
                # 解码
                msg += rec.decode(self._encoding)
                print(msg)
                print()
                # 文本接受是否完毕，因为python socket不能自己判断接收数据是否完毕，
                # 所以需要自定义协议标志数据接受完毕
                if msg.strip().endswith('over'):
                    msg = msg[:-4]
                    break
            ids, urls = msg.split('|')
            ids = ids.split(',')
            urls = urls.split(',')
            print(ids)
            print()
            print(urls)
            print()

            for index in range(len(ids)):
                imageName = os.path.basename(urls[index])  # 文件名称（包含后缀）
                img_path = config.save_path + imageName  # 本地地址
                # 保存OSS图片到本地
                response = requests.get(urls[index])
                local_image = Image.open(BytesIO(response.content))
                local_image.save(img_path)
                feature = fe.execute(img_path)
                # 删除本地图片
                if os.path.exists(img_path):
                    os.remove(img_path)
                else:
                    print('删除图片失败:', img_path)

                # 上传es
                doc = {'id': ids[index], 'feature': feature}
                es.index(config.elasticsearch_index, body=doc, id=ids[index])  # 保存到elasticsearch
            # 返回成功码
            self._socket.send("200".encode(self._encoding))
        except Exception as identifier:
            # 返回错误码
            self._socket.send("500".encode(self._encoding))
            print(identifier)
        finally:
            self._socket.close()
        print("任务结束.....")


if __name__ == '__main__':
    es = Elasticsearch(
        hosts=[
            "http://" + config.elasticsearch_name + ":" + config.elasticsearch_pass + "@" + config.elasticsearch_url
            + ":" + config.elasticsearch_port + "/"], timeout=3600)
    fe = FeatureExtractor()

    # 创建服务器套接字
    serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # 获取本地主机名称
    host = socket.gethostname()
    # 设置一个端口
    port = 12345
    # 将套接字与本地主机和端口绑定
    serversocket.bind((host, port))
    # 设置监听最大连接数
    serversocket.listen(10)
    # 模型创建
    print("等待连接")
    try:
        while True:
            # 获取一个客户端连接
            clientsocket, addr = serversocket.accept()
            print("连接地址:%s" % str(addr))
            try:
                t = ServerThreading(clientsocket)  # 为每一个请求开启一个处理线程
                t.start()
            except Exception as identifier:
                print(identifier)
    finally:
        serversocket.close()
