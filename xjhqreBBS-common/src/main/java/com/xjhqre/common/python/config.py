"""
以图搜图配置文件
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
