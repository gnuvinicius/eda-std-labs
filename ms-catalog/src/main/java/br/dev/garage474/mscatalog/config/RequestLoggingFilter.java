package br.dev.garage474.mscatalog.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Enumeration;

public class RequestLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            log.info(">>> Incoming request: {} {}",
                    httpRequest.getMethod(),
                    httpRequest.getRequestURI());
            log.info(">>> Host: {}", httpRequest.getHeader("Host"));
            log.info(">>> Origin: {}", httpRequest.getHeader("Origin"));
            log.info(">>> X-Forwarded-For: {}", httpRequest.getHeader("X-Forwarded-For"));
            log.info(">>> X-Forwarded-Proto: {}", httpRequest.getHeader("X-Forwarded-Proto"));
            log.info(">>> X-Real-IP: {}", httpRequest.getHeader("X-Real-IP"));
            log.info(">>> Remote address: {}", httpRequest.getRemoteAddr());

            Enumeration<String> headerNames = httpRequest.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    log.debug(">>> Header [{}]: {}", headerName, httpRequest.getHeader(headerName));
                }
            }
        } catch (Exception e) {
            log.error("erro ao logar request: {}", e.getMessage(), e);
            throw e;
        }

        chain.doFilter(request, response);
    }
}
