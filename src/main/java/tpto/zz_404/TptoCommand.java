package tpto.zz_404;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import static net.minecraft.server.command.CommandManager.*;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Supplier;

public class TptoCommand implements ModInitializer {
	public static final String MOD_ID = "tpto-command";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(
				(dispatcher,
				 registryAccess,
				 environment) -> dispatcher.register(literal("tpto")
						.then(argument("name", EntityArgumentType.player())
								.executes(context -> {
									final ServerPlayerEntity source = context.getSource().getPlayer();
									final ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "name");
									final var feedback = tpto(Objects.requireNonNull(source), player);
									context.getSource().sendFeedback(feedback, true);
									return 1;
								})))
		);
	}

	private @NotNull Supplier<Text> tpto(@NotNull ServerPlayerEntity source, @NotNull ServerPlayerEntity player) {
		source.requestTeleport(player.getX(), player.getY(), player.getZ());
		return () -> Text.literal(String.format("Teleported %s to %s", source.getName().getString(), player.getName().getString()));
	}
}