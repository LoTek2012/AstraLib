package io.github.lotek2012.astralib.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import io.github.lotek2012.astralib.gravity.GravityManager;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class GravityCommand {
    private GravityCommand() {
    }

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("gravity")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.literal("set")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg())
                                        .executes(context -> {
                                            double gravity = DoubleArgumentType.getDouble(context, "value");
                                            var targets = EntityArgument.getEntities(context, "targets");
                                            for (Entity entity : targets) {
                                                GravityManager.setGravity(entity, gravity);
                                                context.getSource().sendSuccess(
                                                        () -> Component.translatable("command.astralib.gravity.set",
                                                                entity.getDisplayName(), gravity),
                                                        true);
                                            }
                                            return targets.size();
                                        })))
                        .then(Commands.literal("get")
                                .executes(context -> {
                                    var targets = EntityArgument.getEntities(context, "targets");
                                    for (Entity entity : targets) {
                                        double gravity = GravityManager.getGravity(entity);
                                        context.getSource().sendSuccess(
                                                () -> Component.translatable("command.astralib.gravity.get",
                                                        entity.getDisplayName(), gravity),
                                                false);
                                    }
                                    return targets.size();
                                }))
                        .then(Commands.literal("reset")
                                .executes(context -> {
                                    var targets = EntityArgument.getEntities(context, "targets");
                                    for (Entity entity : targets) {
                                        GravityManager.resetGravity(entity);
                                        double gravity = GravityManager.getGravity(entity);
                                        context.getSource().sendSuccess(
                                                () -> Component.translatable("command.astralib.gravity.reset",
                                                        entity.getDisplayName(), gravity),
                                                true);
                                    }
                                    return targets.size();
                                }))));
    }
}
