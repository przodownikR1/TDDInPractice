FROM openjdk:8-jdk-alpine
MAINTAINER przodownikR1
VOLUME /tmp
EXPOSE 9999
WORKDIR /gitInfo
RUN rm -rf /log
RUN mkdir /log && chmod 0755 /log
ENV JAVA_OPTS=""
COPY build/libs/gitInfo-0.0.1-SNAPSHOT.jar /data/gitInfo.jar
CMD ["bash"]
