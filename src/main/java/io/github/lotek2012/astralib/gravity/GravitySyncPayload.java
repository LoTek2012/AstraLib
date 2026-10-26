package io.github.lotek2012.astralib.gravity;

import java.util.UUID;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record GravitySyncPayload(UUID entityId, double gravity) implements CustomPacketPayload {
    public static final Type<GravitySyncPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath("astralib", "gravity_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, GravitySyncPayload> STREAM_CODEC = StreamCodec.of(
            (buffer, payload) -> {
                buffer.writeUUID(payload.entityId);
                buffer.writeDouble(payload.gravity);
            },
            buffer -> new GravitySyncPayload(buffer.readUUID(), buffer.readDouble()));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
