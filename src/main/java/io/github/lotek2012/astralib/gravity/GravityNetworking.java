package io.github.lotek2012.astralib.gravity;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public final class GravityNetworking {
    private GravityNetworking() {
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToClient(
                GravitySyncPayload.TYPE,
                GravitySyncPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(
                        () -> GravitySyncCache.setGravity(payload.entityId(), payload.gravity())));
    }

    static void sendGravity(ServerPlayer player, double gravity) {
        PacketDistributor.sendToPlayer(player, new GravitySyncPayload(player.getUUID(), gravity));
    }
}
