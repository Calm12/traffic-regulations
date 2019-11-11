#!/bin/bash

while :;
do
	java -jar -server -Dfile.encoding=UTF-8 -Xmx1G pdd.jar > logs/stdout.log 2>&1

	[ $? -ne 2 ] && break
	sleep 10;
done
