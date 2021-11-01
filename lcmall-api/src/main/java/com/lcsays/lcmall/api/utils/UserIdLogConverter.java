package com.lcsays.lcmall.api.utils;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @Author: lichuang
 * @Date: 21-11-1 16:27
 */

public class UserIdLogConverter extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        return "-";
    }
}
