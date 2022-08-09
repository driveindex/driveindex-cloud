package io.github.driveindex.common.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author sgpublic
 * @Date 2022/8/6 0:52
 */
@Data
public class LoggingData implements Serializable {
    private String threadName;
    private Level level;
    private String message;
    private String formattedMessage;
    private String loggerName;
    private StackTraceElement[] callerData;
    private Boolean hasCallerData;
    private Long timeStamp;

    public static LoggingData of(ILoggingEvent event) {
        LoggingData copy = new LoggingData();
        copy.threadName = event.getThreadName();
        copy.level = event.getLevel();
        copy.message = event.getMessage();
        copy.formattedMessage = event.getFormattedMessage();
        copy.loggerName = event.getLoggerName();
        copy.hasCallerData = event.hasCallerData();
        copy.callerData = event.getCallerData();
        copy.timeStamp = event.getTimeStamp();
        return copy;
    }

    @Override
    public String toString() {
        return "LoggingData{" +
                "threadName='" + threadName + '\'' +
                ", level=" + level +
                ", message='" + message + '\'' +
                ", formattedMessage='" + formattedMessage + '\'' +
                ", loggerName='" + loggerName + '\'' +
                ", callerData=" + Arrays.toString(callerData) +
                ", hasCallerData=" + hasCallerData +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
