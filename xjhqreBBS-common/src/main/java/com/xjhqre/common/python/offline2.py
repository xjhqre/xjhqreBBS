import numpy as np
import os
import oss2
import requests
import sys
import tensorflow as tf
from PIL import Image
from elasticsearch import Elasticsearch
from io import BytesIO
from keras_applications.resnet50 import ResNet50
from numpy import linalg as LA
from tensorflow.keras.applications.resnet50 import preprocess_input
from tensorflow.keras.preprocessing import image
from tensorflow.python import keras
from tensorflow.python.keras.backend import set_session

'''
    提取图片特征向量上传阿里云OSS
'''

"""
以图搜图配置文件，批量处理
"""
# TODO 修改保存地址
save_path = 'G:\\workspace\\xjhqreBBS\\xjhqreBBS-picture\\src\\main\\resources\\upload\\'

types = [".jpg", ".jpeg", ".gif", ".png", ".JPG", ".JPEG", ".GIF", ".PNG"]

elasticsearch_index = "xjhqrebbs-imgsearch"

elasticsearch_url = '1.15.88.204'
elasticsearch_port = "9201"
elasticsearch_name = "elastic"
elasticsearch_pass = "xjhqre"

# OSS
AccessKeyId = "LTAI5tRA8NFprYduUmGAuXAi"
AccessKeySecret = "UgorzXce3lbW8n8hUMgomakhRtbfVO"
EndPoint = "oss-cn-hangzhou.aliyuncs.com"
bucket = "xjhqre-bbs"
folder = 'test/'
pic_oss_url = "https://xjhqre-bbs.oss-cn-hangzhou.aliyuncs.com/" + folder

# 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
auth = oss2.Auth(AccessKeyId, AccessKeySecret)
# yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
# 填写Bucket名称。
bucket = oss2.Bucket(auth, EndPoint, bucket)


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


if __name__ == '__main__':
    # 获取 java 里传来的参数，a[0]: 图片的OSS地址
    ids = str(sys.argv[1])[1:-1].split(', ')
    urls = str(sys.argv[2])[1:-1].split(', ')

    es = Elasticsearch(
        hosts=[
            "http://" + elasticsearch_name + ":" + elasticsearch_pass + "@" + elasticsearch_url
            + ":" + elasticsearch_port + "/"], timeout=3600)
    fe = FeatureExtractor()

    try:
        for index in range(len(ids)):
            imageName = os.path.basename(urls[index])  # 文件名称（包含后缀）
            img_path = save_path + imageName  # 本地地址
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
            es.index(elasticsearch_index, body=doc)  # 保存到elasticsearch
        print(200, end='')
    except:
        print("解析图片出错")
