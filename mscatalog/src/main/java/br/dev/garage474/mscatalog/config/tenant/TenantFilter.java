package br.dev.garage474.mscatalog.config.tenant;

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
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Filter that validates the presence and format of the tenantId header and stores it in TenantContext.
 * <p>
 * This implementation allows a configurable list of endpoints that will bypass tenant validation.
 * Swagger/OpenAPI endpoints are included by default and more paths can be added at runtime using
 * `TenantFilter.addExcludedPath(...)`.
 */
@Component
public class TenantFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(TenantFilter.class);
    public static final String TENANT_HEADER = "tenantId";

    // Mutable, thread-safe list so other components or tests can add excluded paths at runtime.
    private static final CopyOnWriteArrayList<String> EXCLUDED_PATHS = new CopyOnWriteArrayList<>(List.of(
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
    ));

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * Add an excluded path pattern at runtime (Ant-style patterns supported).
     */
    public static void addExcludedPath(String pattern) {
        if (pattern != null && !pattern.isBlank()) {
            EXCLUDED_PATHS.addIfAbsent(pattern.trim());
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String tenantHeader = request.getHeader(TENANT_HEADER);

            // Allow preflight requests and explicitly excluded endpoints to bypass tenant validation
            if (isExcluded(request) || "OPTIONS".equalsIgnoreCase(request.getMethod())) {
                log.debug("TenantFilter - skipping tenant validation for excluded path {} {}", request.getMethod(), request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            if (tenantHeader == null || tenantHeader.isBlank()) {
                log.warn("Missing tenantId header for request {} {}", request.getMethod(), request.getRequestURI());
                sendBadRequest(response, "tenantId header is required");
                return;
            }

            try {
                UUID tenantId = UUID.fromString(tenantHeader.trim());
                TenantContext.setTenantId(tenantId);
                filterChain.doFilter(request, response);
            } catch (IllegalArgumentException ex) {
                log.warn("Invalid tenantId header: {} for request {} {}", tenantHeader, request.getMethod(), request.getRequestURI());
                sendBadRequest(response, "tenantId header is invalid");
            }
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
