package io.github.driveindex.common.logback.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import io.github.driveindex.common.logback.LoggingData;
import io.github.driveindex.common.logback.LoggingMQ;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sgpublic
 * @Date 2022/8/5 21:17
 */
public class LoggingMQAppender extends AppenderBase<ILoggingEvent> {
    @Override
    protected void append(ILoggingEvent event) {
        if (event.getLoggerName().startsWith(LoggingMQ.class.getPackageName())) return;
        LoggingMQ.produce(LoggingData.of(event));
    }
}
