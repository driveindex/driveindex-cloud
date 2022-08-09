package io.github.driveindex.common.logback.encoder;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import io.github.driveindex.common.logback.converter.TraceConverter;

/**
 * @author sgpublic
 * @Date 2022/8/5 18:30
 */
public class ConsolePatternLayoutEncoder extends PatternLayoutEncoder {
    static {
        PatternLayout.DEFAULT_CONVERTER_MAP.put("trace", TraceConverter.class.getName());
    }
}
