package io.github.lotek2012.astralib;

import io.github.lotek2012.astralib.command.GravityCommand;
import io.github.lotek2012.astralib.gravity.GravityNetworking;
import io.github.lotek2012.astralib.gravity.GravityPhysics;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(AstraLibMod.MOD_ID)
public class AstraLibMod {
    public static final String MOD_ID = "astralib";

    public AstraLibMod(IEventBus modEventBus) {
        modEventBus.addListener(GravityNetworking::register);
        NeoForge.EVENT_BUS.addListener(GravityCommand::register);
        NeoForge.EVENT_BUS.addListener(GravityPhysics::onEntityTick);
        NeoForge.EVENT_BUS.addListener(GravityPhysics::onPlayerLogout);
    }
}
