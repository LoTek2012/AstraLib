package io.github.lotek2012.astralib.gravity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public final class GravityManager {
    private static final String GRAVITY_OVERRIDE_KEY = "AstraLibGravityOverride";
    private static final Map<ResourceKey<Level>, Double> DIMENSION_GRAVITIES = new ConcurrentHashMap<>();

    static {
        DIMENSION_GRAVITIES.put(Level.OVERWORLD, 1.0);
        DIMENSION_GRAVITIES.put(Level.NETHER, 1.0);
        DIMENSION_GRAVITIES.put(Level.END, 1.0);
    }

    private GravityManager() {
    }

    /**
     * Returns the entity's override, or its current dimension's default gravity.
     */
    public static double getGravity(Entity entity) {
        if (entity.getPersistentData().contains(GRAVITY_OVERRIDE_KEY)) {
            return entity.getPersistentData().getDouble(GRAVITY_OVERRIDE_KEY);
        }
        return getDimensionGravity(entity.level().dimension());
    }

    /**
     * Sets a persistent gravity override for this entity.
     */
    public static void setGravity(Entity entity, double gravity) {
        validateGravity(gravity);
        entity.getPersistentData().putDouble(GRAVITY_OVERRIDE_KEY, gravity);
    }

    /**
     * Removes the entity override so its current dimension default applies.
     */
    public static void resetGravity(Entity entity) {
        entity.getPersistentData().remove(GRAVITY_OVERRIDE_KEY);
    }

    /**
     * Returns the configured gravity for a dimension, defaulting to 1.0.
     */
    public static double getDimensionGravity(ResourceKey<Level> dimension) {
        return DIMENSION_GRAVITIES.getOrDefault(dimension, 1.0);
    }

    /**
     * Sets the default gravity inherited by entities without an override in this dimension.
     */
    public static void setDimensionGravity(ResourceKey<Level> dimension, double gravity) {
        validateGravity(gravity);
        DIMENSION_GRAVITIES.put(dimension, gravity);
    }

    private static void validateGravity(double gravity) {
        if (!Double.isFinite(gravity)) {
            throw new IllegalArgumentException("Gravity must be a finite number");
        }
    }
}
