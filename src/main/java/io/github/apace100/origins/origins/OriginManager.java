package io.github.apace100.origins.origins;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import io.github.apace100.origins.network.ModPacketHandler;
import io.github.apace100.origins.network.OriginListMessage;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkManager;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkDirection;

public class OriginManager extends JsonReloadListener {
	
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	
	public static OriginListMessage MSG;

	public OriginManager() {
		super(GSON, "origins");
	}

	
	@Override
	protected Map<ResourceLocation, JsonObject> prepare(IResourceManager resourceManagerIn, IProfiler profilerIn) {
		Origins.clear();
		return super.prepare(resourceManagerIn, profilerIn);
	}


	@Override
	protected void apply(Map<ResourceLocation, JsonObject> splashList, IResourceManager resourceManagerIn,
			IProfiler profilerIn) {

		splashList.forEach((rl, jo) -> {
			try {
				Origin ra = Origin.read(rl.toString(), jo);
				if(!Origins.REGISTRY.containsKey(rl)) {
					Origins.register(ra);
				}
			} catch(Exception e) {
				System.err.println("WARNING: There was a problem reading Origin file " + rl.toString() + " (skipping): " + e.getMessage());
			}
		});
		
		MSG = null;
	}
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event) {
		if(MSG == null) {
			MSG = new OriginListMessage(Origins.REGISTRY.values());
		}
		NetworkManager mngr = ((ServerPlayerEntity)event.getPlayer()).connection.netManager;
		ModPacketHandler.INSTANCE.sendTo(MSG.getForPlayer(event.getPlayer()), mngr, NetworkDirection.PLAY_TO_CLIENT);
	}
}
