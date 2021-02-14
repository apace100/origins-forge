package io.github.apace100.origins.command;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import io.github.apace100.origins.origins.Origin;
import io.github.apace100.origins.origins.Origins;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class OriginArgument implements ArgumentType<Origin> {
   public static final DynamicCommandExceptionType ORIGIN_NOT_FOUND = new DynamicCommandExceptionType((p_208663_0_) -> {
      return new TranslationTextComponent("origin.origin_not_found", p_208663_0_);
   });

   public static OriginArgument origin() {
      return new OriginArgument();
   }

   public static Origin getOrigin(CommandContext<CommandSource> context, String name) throws CommandSyntaxException {
      return context.getArgument(name, Origin.class);
   }

   public Origin parse(StringReader p_parse_1_) throws CommandSyntaxException {
      ResourceLocation resourcelocation = ResourceLocation.read(p_parse_1_);
      try {
    	  return Origins.getByResourceLocation(resourcelocation);
      } catch(IllegalArgumentException e) {
    	  throw ORIGIN_NOT_FOUND.create(resourcelocation);
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> p_listSuggestions_1_, SuggestionsBuilder p_listSuggestions_2_) {
      return ISuggestionProvider.suggestIterable(Origins.getAll().stream().map(Origin::getRegistryName).collect(Collectors.toList()), p_listSuggestions_2_);
   }
}