FROM openjdk:8

MAINTAINER Anton Calm <1calm2@gmail.com>

RUN groupadd --gid 1000 node \
&& useradd --uid 1000 --gid 1000 --shell /bin/bash node

RUN mkdir /var/pdd && chown node:node /var/pdd

USER node

CMD ./docker/wait-for-it.sh db:3306 -- java -jar -server -Dfile.encoding=UTF-8 -Xmx1G pdd.jar
