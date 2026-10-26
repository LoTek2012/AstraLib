package io.github.lotek2012.astralib.gravity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public final class GravityPhysics {
    private static final Map<UUID, Double> LAST_SYNCED_GRAVITY = new ConcurrentHashMap<>();

    private GravityPhysics() {
    }

    public static void onEntityTick(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();
        boolean clientSide = entity.level().isClientSide;
        if (entity instanceof ServerPlayer player) {
            syncPlayerGravity(player);
        }
        if (entity.isPassenger() || entity.isNoGravity()) {
            return;
        }

        double vanillaGravity = entity.getGravity();
        double gravityMultiplier = clientSide
                ? GravitySyncCache.getGravity(entity.getUUID()).orElseGet(() -> GravityManager.getGravity(entity))
                : GravityManager.getGravity(entity);
        if (vanillaGravity == 0.0 || gravityMultiplier == 1.0 || (entity.onGround() && gravityMultiplier >= 0.0)) {
            return;
        }

        double physicsMultiplier = 1.0;
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.isInWater()
                    || livingEntity.isInLava()
                    || livingEntity.isFallFlying()
                    || livingEntity.hasEffect(MobEffects.LEVITATION)
                    || livingEntity instanceof Player playerEntity && playerEntity.getAbilities().flying) {
                return;
            }
            if (livingEntity.getDeltaMovement().y <= 0.0 && livingEntity.hasEffect(MobEffects.SLOW_FALLING)) {
                vanillaGravity = Math.min(vanillaGravity, 0.01);
            }
            physicsMultiplier = 0.98;
        }

        Vec3 movement = entity.getDeltaMovement();
        entity.setDeltaMovement(movement.add(0.0, vanillaGravity * (1.0 - gravityMultiplier) * physicsMultiplier, 0.0));
    }

    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        LAST_SYNCED_GRAVITY.remove(event.getEntity().getUUID());
    }

    private static void syncPlayerGravity(ServerPlayer player) {
        double gravity = GravityManager.getGravity(player);
        Double lastSyncedGravity = LAST_SYNCED_GRAVITY.put(player.getUUID(), gravity);
        if (lastSyncedGravity == null || Double.compare(lastSyncedGravity, gravity) != 0) {
            GravityNetworking.sendGravity(player, gravity);
        }
    }
}
