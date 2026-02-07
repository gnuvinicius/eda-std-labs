package br.dev.garage474.mscatalog.config.infrastructure;

import br.dev.garage474.mscatalog.config.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TenantFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(TenantFilter.class);

    public static final String TENANT_HEADER = "X-USER-ID";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String header = request.getHeader(TENANT_HEADER);
            if (header != null && !header.isBlank()) {
                try {
                    java.util.UUID tenantId = java.util.UUID.fromString(header.trim());
                    TenantContext.setTenantId(tenantId);
                    log.debug("TenantFilter - tenant header found: {} for request {} {}", tenantId, request.getMethod(),
                            request.getRequestURI());
                } catch (IllegalArgumentException ex) {
                    log.warn("TenantFilter - invalid tenant header value (not UUID): {} for request {} {}", header,
                            request.getMethod(), request.getRequestURI());
                }
            } else {
                log.debug("TenantFilter - no tenant header for request {} {}", request.getMethod(),
                        request.getRequestURI());
            }
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
            log.debug("TenantFilter - cleared TenantContext for request {} {}", request.getMethod(),
                    request.getRequestURI());
        }
    }
}
