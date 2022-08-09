package io.github.driveindex.common.logback.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import io.github.driveindex.common.DriveIndexCommon;

/**
 * @author sgpublic
 * @Date 2022/8/5 18:38
 */
public class ConsoleFilter extends Filter<ILoggingEvent> {
    private Level self = Level.INFO;
    private Level out = Level.WARN;

    public void setProfileActive(String profile) {
        boolean check = "prod".equals(profile);
        self = check ? Level.INFO : Level.DEBUG;
        out = check ? Level.WARN : Level.INFO;
    }

    @Override
    public FilterReply decide(ILoggingEvent event) {
        Level target = event.getLoggerName().startsWith(DriveIndexCommon.APPLICATION_PACKAGE) ? self : out;
        return event.getLevel().isGreaterOrEqual(target) ? FilterReply.NEUTRAL : FilterReply.DENY;
    }
}
