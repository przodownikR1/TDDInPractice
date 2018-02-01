FROM java:8
MAINTAINER przodownikR1
VOLUME /tmp
EXPOSE 9999
WORKDIR /gitInfoCollector
RUN rm -rf /log
RUN mkdir /log && chmod 0755 /log
ADD gitInfo-collector.jar gitInfoCollector.jar
RUN sh -c 'touch /gitInfoCollector.jar'
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -jar gitInfoCollector.jar