#!/bin/bash

base=../target
CLASSPATH=$base/lcmall-api-0.1.0.jar
for i in $base/lib/*;
do
    CLASSPATH=$i:"$CLASSPATH";
done

java -classpath .:$CLASSPATH com.lcsays.lcmall.api.tools.WhiteListTools rm-2zepdddh8sh08kd06oo.mysql.rds.aliyuncs.com lcmall lcmall "fdjk78348hU*#()H#^*&" "inb7#mj" "ab8(#HJG9o8jybp8" /Users/lichuang/tmp/200 17
