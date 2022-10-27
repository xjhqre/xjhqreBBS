"""
以图搜图配置文件
"""

save_path = 'G:\\workspace\\xjhqreBBS\\xjhqreBBS-picture\\src\\main\\resources\\upload\\'

types = [".jpg", ".jpeg", ".gif", ".png", ".JPG", ".JPEG", ".GIF", ".PNG"]

elasticsearch_index = "imgsearch"

elasticsearch_url = '116.62.103.149'
elasticsearch_port = 9200

# OSS
AccessKeyId = "LTAI5tRA8NFprYduUmGAuXAi"
AccessKeySecret = "UgorzXce3lbW8n8hUMgomakhRtbfVO"
EndPoint = "oss-cn-hangzhou.aliyuncs.com"
bucket = "xjhqre-bbs"
folder = 'test/'
pic_oss_url = "https://xjhqre-bbs.oss-cn-hangzhou.aliyuncs.com/" + folder
