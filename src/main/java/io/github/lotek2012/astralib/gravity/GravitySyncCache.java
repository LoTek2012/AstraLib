package io.github.lotek2012.astralib.gravity;

import java.util.Map;
import java.util.OptionalDouble;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class GravitySyncCache {
    private static final Map<UUID, Double> GRAVITIES = new ConcurrentHashMap<>();

    private GravitySyncCache() {
    }

    public static OptionalDouble getGravity(UUID entityId) {
        Double gravity = GRAVITIES.get(entityId);
        return gravity == null ? OptionalDouble.empty() : OptionalDouble.of(gravity);
    }

    static void setGravity(UUID entityId, double gravity) {
        GRAVITIES.put(entityId, gravity);
    }
}
