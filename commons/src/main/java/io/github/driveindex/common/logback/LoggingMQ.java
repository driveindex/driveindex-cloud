package io.github.driveindex.common.logback;

import ch.qos.logback.classic.Level;
import feign.Contract;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 基于消息队列的异步日志
 * @author sgpublic
 * @Date 2022/8/5 21:26
 */
@Component
@EnableFeignClients(clients = LoggingClient.class)
@RequiredArgsConstructor
public class LoggingMQ {
    // 使用线程安全的 LinkedBlockingQueue 做消息队列
    private static final Queue<LoggingData> message = new LinkedBlockingQueue<>();
    public static void produce(LoggingData event) {
        // 消息累计过多时停止累积
        if (message.size() > 512) return;
        // 当消息堆积时减缓日志累积速度
        if (message.size() > 256 && !event.getLevel().isGreaterOrEqual(Level.WARN)) return;
        message.add(event);
    }

    private LoggingHandler handler;
    @Autowired
    public void setLoggingClient(LoggingClient client) {
        this.handler = new LoggingHandler(client);
    }

    @PostConstruct
    protected void onConstruct() {
        executor.execute(handler);
    }

    private final ThreadPoolTaskExecutor executor;

    private record LoggingHandler(LoggingClient client) implements Runnable {
        @Override
        public void run() {
            if (client == null) {
                return;
            }
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    if (message.isEmpty()) continue;
                    LoggingData data = message.peek();
                    client.sendLoggingEvent(data);
                } catch (Exception ignore) { }
            }
        }
    }
}
