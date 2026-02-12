package br.dev.garage474.mscatalog.config.tenant;

import java.util.Optional;
import java.util.UUID;

/**
 * Thread-local holder for the current request's tenantId.
 */
public final class TenantContext {

    private static final ThreadLocal<UUID> TENANT = new ThreadLocal<>();

    private TenantContext() {}

    public static void setTenantId(UUID tenantId) {
        TENANT.set(tenantId);
    }

    public static Optional<UUID> getTenantId() {
        return Optional.ofNullable(TENANT.get());
    }

    public static void clear() {
        TENANT.remove();
    }
}

