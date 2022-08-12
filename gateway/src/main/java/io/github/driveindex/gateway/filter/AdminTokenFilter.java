package io.github.driveindex.gateway.filter;

import io.github.driveindex.common.DriveIndexCommon;
import io.github.driveindex.common.dto.result.FailedResult;
import io.github.driveindex.gateway.util.JwtChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 用于验证需管理员权限的接口
 *
 * @author sgpublic
 * @Date 2022/8/4 9:32
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminTokenFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder builder = request.mutate();
        // 删除外部可能直接提交的认证信息预防攻击
        builder.headers(k -> k.remove(DriveIndexCommon.SECURITY_HEADER));

        String path = request.getPath().value();

        // 如果访问的接口为登录接口或不为管理员接口则放行
        if (path.startsWith("/api/login") || !path.startsWith("/api/admin")) {
            return chain.filter(exchange);
        }

        String auth = null;
        List<String> list = request.getHeaders().get(DriveIndexCommon.TOKEN_KEY);
        if (list != null && !list.isEmpty()) {
            auth = list.get(0);
        }
        // 删除 token 信息
        builder.headers(k -> k.remove(DriveIndexCommon.TOKEN_KEY));

        // 若提交了 token 且 token 有效则放行
        if (auth != null) {
            String tag = JwtChecker.findTag(auth);
            if (tag != null) {
                // 添加认证信息
                builder.header(DriveIndexCommon.SECURITY_HEADER, tag);
                return chain.filter(exchange.mutate().request(builder.build()).build());
            }
        }

        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        byte[] result = FailedResult.EXPIRED_TOKEN.toString().getBytes();
        return response.writeWith(Mono.just(response.bufferFactory().wrap(result)));
    }
}
