package io.github.apace100.origins.command;

import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;

import io.github.apace100.origins.capabilities.OriginCapability;
import io.github.apace100.origins.network.NetworkHelper;
import io.github.apace100.origins.network.SyncOriginMessage;
import io.github.apace100.origins.origins.Origin;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class OriginCommand {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(
			Commands.literal("origin").requires(cs -> cs.hasPermissionLevel(2))
				.then(Commands.literal("set")
						.then(Commands.argument("targets", EntityArgument.players())
								.then(Commands.argument("origin", OriginArgument.origin())
										.executes((command) -> {
											int i = 0;
											Collection<ServerPlayerEntity> targets = EntityArgument.getPlayers(command, "targets");
											Origin o = OriginArgument.getOrigin(command, "origin");
											for(ServerPlayerEntity target : targets) {
												setOrigin(target, o);
												i++;
											}
											if (targets.size() == 1) {
												command.getSource().sendFeedback(new TranslationTextComponent("commands.origin.set.success.single", targets.iterator().next().getDisplayName(), o.getDisplayName()), true);
											} else {
												command.getSource().sendFeedback(new TranslationTextComponent("commands.origin.set.success.multiple", targets.size(), o.getDisplayName()), true);
											}
											return i;
										})))));
	}

	private static void setOrigin(PlayerEntity player, Origin origin) {
		player.getCapability(OriginCapability.CAPABILITY).ifPresent((r) -> {
			r.setOrigin(origin, player);
			NetworkHelper.sendToAllTracking(new SyncOriginMessage(player), player);
		});
	}
}
