# -*- coding: utf-8 -*-

import oss2

from com.xjhqre.common.python import config

# 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
auth = oss2.Auth(config.AccessKeyId, config.AccessKeySecret)
# yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
# 填写Bucket名称。
bucket = oss2.Bucket(auth, config.EndPoint, config.bucket)


# 上传文件到指定bucket
def upload_2_oss(file_name, file_path):
    # 例  file_name：test/imageName.jpg
    return bucket.put_object_from_file(file_name, file_path)
