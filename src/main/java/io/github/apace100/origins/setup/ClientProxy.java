package io.github.apace100.origins.setup;

import io.github.apace100.origins.KeyBindings;
import io.github.apace100.origins.block.TemporarySpiderWebBlock;
import io.github.apace100.origins.entity.SuperEnderPearlEntity;
import io.github.apace100.origins.origins.ChooseOriginScreen;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy implements IProxy {

	@Override
	public World getClientWorld() {
		return Minecraft.getInstance().world;
	}

	@Override
	public void setup() {
		KeyBindings.register();
	}
	
	@Override
	public void clientSetup() {
		RenderTypeLookup.setRenderLayer(Blocks.COBWEB, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(TemporarySpiderWebBlock.INSTANCE, RenderType.getCutout());
		RenderingRegistry.registerEntityRenderingHandler(SuperEnderPearlEntity.TYPE, erm -> new SpriteRenderer<>(erm, Minecraft.getInstance().getItemRenderer()));
	}

	@Override
	public void openOriginGui() {
		Minecraft.getInstance().displayGuiScreen(new ChooseOriginScreen());
	}

	@Override
	public PlayerEntity getClientPlayer() {
		return Minecraft.getInstance().player;
	}
}
