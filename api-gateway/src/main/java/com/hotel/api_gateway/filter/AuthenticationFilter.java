package com.hotel.api_gateway.filter;

import com.hotel.api_gateway.exception.ForbiddenAccessException;
import com.hotel.api_gateway.exception.UnauthorizedUserException;
import com.hotel.api_gateway.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    // Publicly accessible endpoints
    private final List<String> openApiEndpoints = List.of(
            "/swagger-ui/",
            "/v3/api-docs/",
            "/api/auth/"
    );

    // Mapping of routes to allowed roles
    private final Map<String, List<String>> routeRoleMap = Map.of(
            "/api/rooms", List.of("ADMIN", "MANAGER"),
            "/api/rooms/availableRooms", List.of("ADMIN", "MANAGER", "USER"),
            "/api/inventory", List.of("ADMIN", "MANAGER"),
            "/api/bookings/available", List.of("ADMIN", "MANAGER", "USER"),
            "/api/bookings", List.of("ADMIN", "USER"),
            "/api/guests", List.of("ADMIN", "USER"),
            "/api/staff", List.of("ADMIN", "MANAGER"),
            "/api/bills", List.of("ADMIN", "USER")
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();

        if (isOpenApi(path)) {
            return chain.filter(exchange);
        }

        List<String> authHeaders = exchange.getRequest().getHeaders().getOrEmpty("Authorization");

        if (authHeaders.isEmpty()) {
            throw new UnauthorizedUserException("Authorization header is missing");
        }

        String token = authHeaders.get(0);

        try {
            boolean valid = jwtUtil.validateToken(token);
            if (!valid) {
                throw new UnauthorizedUserException("Token is invalid or expired");
            }

            String role = jwtUtil.extractRole(token);

            if (!isAuthorized(path, role)) {
                throw new ForbiddenAccessException("Access Denied: Role " + role + " is not permitted for this route.");
            }

        } catch (UnauthorizedUserException | ForbiddenAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new UnauthorizedUserException("Failed to process token: " + e.getMessage());
        }

        return chain.filter(exchange);
    }

    private boolean isOpenApi(String path) {
        return openApiEndpoints.stream().anyMatch(path::contains);
    }

    private boolean isAuthorized(String path, String role) {
        for (Map.Entry<String, List<String>> entry : routeRoleMap.entrySet()) {
            String route = entry.getKey();
            List<String> allowedRoles = entry.getValue();

            if (path.startsWith(route)) {
                return allowedRoles.contains(role.toUpperCase());
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
