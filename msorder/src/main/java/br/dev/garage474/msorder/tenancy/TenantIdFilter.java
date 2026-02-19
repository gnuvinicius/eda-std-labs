package br.dev.garage474.msorder.tenancy;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class TenantIdFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(TenantIdFilter.class);

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // Mutable, thread-safe list so other components or tests can add excluded paths at runtime.
    private static final List<String> EXCLUDED_PATHS = List.of(
            // OpenAPI / Swagger UI (springdoc) defaults
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/webjars/**",
            // Health and actuator can be excluded if desired (uncomment if needed)
            // "/actuator/**",
            // other public endpoints can be added here
            "/public/**"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String tenantIdHeader = request.getHeader("tenantId");

        if (isExcluded(request) || "OPTIONS".equalsIgnoreCase(request.getMethod())) {
            log.debug("TenantFilter - skipping tenant validation for excluded path {} {}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        if (tenantIdHeader == null || tenantIdHeader.isBlank()) {
            sendBadRequest(response, "tenantId header is required");
            return;
        }

        try {
            UUID tenantId = UUID.fromString(tenantIdHeader.trim());
            TenantContext.setTenantId(tenantId);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("invalid tenantId header: {}", e.getMessage(), e);
            sendBadRequest(response, "tenantId header must be a valid UUID");
        } finally {
            TenantContext.clear();
        }
    }

    private boolean isExcluded(HttpServletRequest request) {
        String path = request.getRequestURI();
        String context = request.getContextPath();
        if (context != null && !context.isEmpty() && path.startsWith(context)) {
            path = path.substring(context.length());
        }
        for (String pattern : EXCLUDED_PATHS) {
            if (pathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    private void sendBadRequest(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{ \"error\": \"" + message + "\" }");
    }
}

