package io.github.apace100.origins.config;

import java.nio.file.Path;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class Config {

	public static final String CATEGORY_GENERAL = "general";
	
	private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
	
	public static ForgeConfigSpec COMMON_CONFIG;
	
	public static BooleanValue NEW_ORIGIN_ON_DEATH;
	
	static {
		COMMON_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);
		
		NEW_ORIGIN_ON_DEATH = COMMON_BUILDER.comment("Whether players should choose a new Origin on death.")
				.define("newOriginOnDeath", false);
		
		COMMON_BUILDER.pop();
		
		COMMON_CONFIG = COMMON_BUILDER.build();
	}
	
	public static void loadConfig(ForgeConfigSpec spec, Path path) {
		final CommentedFileConfig configData = CommentedFileConfig.builder(path)
				.sync()
				.autosave()
				.writingMode(WritingMode.REPLACE)
				.build();
		configData.load();
		spec.setConfig(configData);
	}
}
