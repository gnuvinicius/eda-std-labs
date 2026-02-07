package br.dev.garage474.msestoque.config.infrastructure;

import org.springframework.boot.web.servlet.FilterRegistrationBean;

public class TenantFilterRegistration {

    public FilterRegistrationBean<TenantFilter> tenantFilter() {
        FilterRegistrationBean<TenantFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new TenantFilter());
        // apply to all urls so tenant header is captured for all controllers
        reg.addUrlPatterns("/*");
        reg.setName("tenantFilter");
        reg.setEnabled(true);
        reg.setOrder(1);
        return reg;
    }
}
