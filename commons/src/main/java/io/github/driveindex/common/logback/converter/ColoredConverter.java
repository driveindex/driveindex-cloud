package io.github.driveindex.common.logback.converter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sgpublic
 * @Date 2022/8/4 15:15
 */
public class ColoredConverter extends CompositeConverter<ILoggingEvent> {
    // 利用 HashMap 提高性能
    private static final Map<Level, String> colored = new HashMap<>();

    public static final String FG_TRACE = "\u001B[1;37;1m";

    static {
        colored.put(Level.TRACE, FG_TRACE);
        colored.put(Level.DEBUG, "\u001B[1;36;1m");
        colored.put(Level.INFO, "\u001B[1;32;1m");
        colored.put(Level.WARN, "\u001B[1;33;1m");
        colored.put(Level.ERROR, "\u001B[1;31;1m");
    }

    public static final String FG_END = "\u001B[0m";

    @Override
    protected String transform(ILoggingEvent event, String s) {
        return colored.getOrDefault(event.getLevel(), FG_TRACE) + s + FG_END;
    }
}
