package com.lcsays.lcmall.api.config.logger;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @Author: lichuang
 * @Date: 21-11-1 16:27
 */

public class UserIdLogConverter extends ClassicConverter {

    public static class LoggerConfigHolder {
        private static final ThreadLocal<String> THREAD_LOCAL = ThreadLocal.withInitial(() -> "-");
        public static String get() {
            return THREAD_LOCAL.get();
        }
        public static void set(final String label) {
            THREAD_LOCAL.set(label);
        }
        public static void remove() {
            THREAD_LOCAL.remove();
        }
    }

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        return LoggerConfigHolder.get();
    }
}
