import os
import shutil
import sys
from io import BytesIO

import requests
from PIL import Image
from elasticsearch import Elasticsearch

import config
from feature_extractor import FeatureExtractor
from upload_oss import upload_2_oss

'''
    提取图片特征向量上传阿里云OSS
'''

es = Elasticsearch([{'host': config.elasticsearch_url, 'port': config.elasticsearch_port}], timeout=3600)
fe = FeatureExtractor()


def moveFile(srcfile, dstPath):  # 移动函数
    if not os.path.isfile(srcfile):
        print("%s not exist!" % (srcfile))
    else:
        fpath, fname = os.path.split(srcfile)  # 分离文件名和路径
        if not os.path.exists(dstPath):
            os.makedirs(dstPath)  # 创建路径
        shutil.move(srcfile, dstPath + fname)  # 移动文件
        print("move %s -> %s" % (srcfile, dstPath + fname))


if __name__ == '__main__':
    # 获取 java 里传来的参数，a[0]: 图片的OSS地址
    a = []
    for i in range(1, len(sys.argv)):
        a.append((str(sys.argv[i])))

    img_url = "https://xjhqre-bbs.oss-cn-hangzhou.aliyuncs.com/test/wallhaven-8ogod1.jpg"
    imageName = os.path.basename(img_url)  # 文件名称（包含后缀）
    _, file_suffix = os.path.splitext(imageName)  # 文件后缀

    # 保存图片到本地
    response = requests.get(img_url)
    image = Image.open(BytesIO(response.content))
    image.save(config.save_path + imageName)

    if file_suffix not in config.types:
        print("格式出错：" + imageName)
    try:
        feature = fe.execute(config.save_path + imageName)
    except Exception as e:
        print("出现异常：" + str(e))
    else:
        # 上传OSS
        status = upload_2_oss(config.folder + imageName, config.save_path + imageName).resp.status
        if status == 200:
            # 上传es
            imgOssUrl = config.pic_oss_url + imageName
            doc = {'url': imgOssUrl, 'feature': feature,
                   'name': imageName}
            es.index(config.elasticsearch_index, body=doc)  # 保存到elasticsearch
            print(200)
        else:
            print("上传OSS异常")
