# -*- coding: utf-8 -*-
import numpy as np
import oss2
import tensorflow as tf
from PIL import Image
from elasticsearch import Elasticsearch
from flask import Flask, request, render_template
from keras_applications.resnet50 import ResNet50
from numpy import linalg as LA
from tensorflow.keras.applications.resnet50 import preprocess_input
from tensorflow.keras.preprocessing import image
from tensorflow.python import keras
from tensorflow.python.keras.backend import set_session

import config

'''
    以图搜图搜索图片
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


app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False

# Read image features
fe = FeatureExtractor()

# elasticsearch没有密码的情况：
# es = Elasticsearch([{'host': elasticsearch_url, 'port': elasticsearch_port}], timeout=3600)
# elasticsearch设置密码的情况：
es = Elasticsearch(
    hosts=["http://" + config.elasticsearch_name + ":" + config.elasticsearch_pass + "@" + config.elasticsearch_url
           + ":" + config.elasticsearch_port + "/"], timeout=3600)


def feature_search(query):
    global es
    # print(query)
    results = es.search(
        index=config.elasticsearch_index,
        body={
            "size": 30,
            "query": {
                "script_score": {
                    "query": {
                        "match_all": {}
                    },
                    "script": {
                        "source": "cosineSimilarity(params.queryVector, doc['feature'])+1.0",
                        "params": {
                            "queryVector": query
                        }
                    }
                }
            }
        })
    hitCount = results['hits']['total']['value']

    if hitCount > 0:
        # if hitCount is 1:
        # print(str(hitCount), ' result')
        # else:
        # print(str(hitCount), 'results')
        answers = []
        max_score = results['hits']['max_score']

        if max_score >= 0.35:
            for hit in results['hits']['hits']:
                if hit['_score'] > 0.5 * max_score:
                    imgurl = hit['_source']['url']
                    name = hit['_source']['name']
                    imgurl = imgurl.replace("#", "%23")
                    answers.append([imgurl, name])
    else:
        answers = []
    return answers


@app.route('/', methods=['GET', 'POST'])
def index():
    if request.method == 'POST':
        file = request.files['query_img']

        # Save query image
        img = Image.open(file.stream)  # PIL image
        # print(file.filename)
        uploaded_img_path = "static/uploaded/" + file.filename
        # print(uploaded_img_path)
        img.save(uploaded_img_path)

        # Run search
        query = fe.execute(uploaded_img_path)
        answers = feature_search(query)

        return render_template('index.html',
                               query_path=uploaded_img_path.replace("#", "%23"),
                               scores=answers)
    else:
        return render_template('index.html')


if __name__ == "__main__":
    app.run("0.0.0.0")
