FROM openjdk:8

MAINTAINER Anton Calm <1calm2@gmail.com>

ARG HOST_UID
ARG HOST_GID

RUN groupadd --gid ${HOST_GID} node \
&& useradd --uid ${HOST_UID} --gid ${HOST_GID} --shell /bin/bash node

RUN mkdir /var/pdd && chown node:node /var/pdd

USER node

CMD ./docker/wait-for-it.sh db:3306 -- java -jar -server -Dfile.encoding=UTF-8 -Xmx1G pdd.jar
