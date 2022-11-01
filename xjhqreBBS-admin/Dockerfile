# 该镜像需要依赖的基础镜像
FROM openjdk:8-jdk-alpine
# 将targer目录下的jar包复制到docker容器/home/springboot目录下面目录下面
ADD ./target/xjhqreBBS-admin-0.0.1-SNAPSHOT.jar /home/springboot/xjhqreBBS-admin.jar
EXPOSE 8080
# 执行命令
CMD ["java","-jar","/home/springboot/springboot-dockerfile.jar"]