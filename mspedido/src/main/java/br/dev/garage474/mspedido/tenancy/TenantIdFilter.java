package br.dev.garage474.mspedido.tenancy;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class TenantIdFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(TenantIdFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String tenantIdHeader = request.getHeader("tenantId");
        if (tenantIdHeader == null || tenantIdHeader.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write("{\"message\":\"tenantId header is required\"}");
            return;
        }

        try {
            UUID tenantId = UUID.fromString(tenantIdHeader.trim());
            TenantContext.setTenantId(tenantId);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("invalid tenantId header: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write("{\"message\":\"tenantId header must be a valid UUID\"}");
        } finally {
            TenantContext.clear();
        }
    }
}

