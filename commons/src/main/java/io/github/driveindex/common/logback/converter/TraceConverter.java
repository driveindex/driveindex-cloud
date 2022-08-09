package io.github.driveindex.common.logback.converter;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @author sgpublic
 * @Date 2022/8/4 15:15
 */
public class TraceConverter extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent event) {
        StackTraceElement data = event.getCallerData()[0];
        return data.getFileName() + ":" + data.getLineNumber();
    }
}
