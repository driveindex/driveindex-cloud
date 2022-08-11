package io.github.driveindex.gateway.filter;

import io.github.driveindex.common.dto.result.FailedResult;
import io.github.driveindex.gateway.util.JwtChecker;
import lombok.RequiredArgsConstructor;
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
@Component
@RequiredArgsConstructor
public class AdminTokenFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<String> strings = exchange.getResponse().getHeaders()
                .get(JwtChecker.SECURITY_HEADER);
        if (strings != null) strings.clear();

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        // 如果访问的接口为登录接口或不为管理员接口则放行
        if (
                path.startsWith("/api/login")
                        || !path.startsWith("/api/admin")
        ) {
            return chain.filter(exchange);
        }

        String auth = JwtChecker.resolveToken(request);
        // 若提交了 token 且 token 有效则放行
        if (auth != null && JwtChecker.validateToken(auth)) {
            // 添加认证信息
            exchange.getResponse().getHeaders()
                    .set(JwtChecker.SECURITY_HEADER, JwtChecker.getUsername(auth));
            return chain.filter(exchange);
        }

        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        byte[] result = FailedResult.EXPIRED_TOKEN.toString().getBytes();
        return response.writeWith(Mono.just(response.bufferFactory().wrap(result)));
    }
}
