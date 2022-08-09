package io.github.driveindex.logging.controller;

import io.github.driveindex.common.dto.result.ResponseData;
import io.github.driveindex.common.dto.result.SampleSuccessResult;
import io.github.driveindex.common.logback.LoggingData;
import io.github.driveindex.logging.h2.repository.LoggingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sgpublic
 * @Date 2022/8/5 22:02
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class LoggingController {
    private final LoggingRepository logging;

    @PostMapping("/log")
    public ResponseData resolveLoggingEvent(LoggingData event) {
        log.debug("receive logging data: " + event);
        logging.insert(event);
        return new SampleSuccessResult();
    }
}
