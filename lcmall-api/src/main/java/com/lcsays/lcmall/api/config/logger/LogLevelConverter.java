package com.lcsays.lcmall.api.config.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @Author: lichuang
 * @Date: 21-11-1 16:27
 */

public class LogLevelConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        Level level = iLoggingEvent.getLevel();
        if (level == Level.DEBUG) {
            return "D";
        } else if (level == Level.INFO) {
            return "I";
        } else if (level == Level.WARN) {
            return "W";
        } else if (level == Level.ERROR) {
            return "E";
        } else {
            return level.toString();
        }
    }
}
