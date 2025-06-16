package org.demo.gateway.filter;

import com.auth0.jwt.interfaces.Claim;
import lombok.AllArgsConstructor;
import org.demo.common.utils.JwtUtils;
import org.demo.gateway.config.AuthProperties;
import org.demo.gateway.config.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private AuthProperties authProperties;
    private JwtProperties jwtProperties;
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        List<String> includePaths = authProperties.getIncludePaths();
        List<String> excludePaths = authProperties.getExcludePaths();

        boolean inExclude = excludePaths.stream().anyMatch(p -> pathMatcher().match(p, path));
        if (inExclude) {
            return chain.filter(exchange);
        }

        boolean inInclude = includePaths.stream().anyMatch(p -> pathMatcher().match(p, path));

        String accessToken = exchange.getRequest().getHeaders().getFirst("Authorization");


        if (!inInclude) {
            if (Objects.isNull(accessToken)) {
                return chain.filter(exchange);
            }
            try {
                return parseJwtAndAddHeader(exchange, chain, accessToken);
            } catch (RuntimeException e) {
                return chain.filter(exchange);
            }
        }

        try {
            return parseJwtAndAddHeader(exchange, chain, accessToken);
        } catch (RuntimeException e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private Mono<Void> parseJwtAndAddHeader(ServerWebExchange exchange, GatewayFilterChain chain, String accessToken) {
        Map<String, Claim> claimMap = JwtUtils.parseJwt(accessToken, jwtProperties.getSecret());
        Long uid = (Long) claimMap.get("user").asMap().get("userId");

        String val = (String) redisTemplate.opsForValue().get(String.valueOf(uid));
        if (Objects.isNull(val)) {
            throw new RuntimeException();
        }
        Long roleId = Long.valueOf(val);

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-User-ID", String.valueOf(uid))
                .header("X-Role-ID", String.valueOf(roleId))
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private PathMatcher pathMatcher() {
        return new AntPathMatcher();
    }

}
