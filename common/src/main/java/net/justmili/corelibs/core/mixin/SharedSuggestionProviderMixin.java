package net.justmili.corelibs.core.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SharedSuggestionProvider.class)
public interface SharedSuggestionProviderMixin {

    // CORELIBS: Mojang mainlined the fix in 1.21.11
    @Definition(id = "resourceLocation", local = @Local(type = ResourceLocation.class))
    @Definition(id = "getNamespace", method = "Lnet/minecraft/resources/ResourceLocation;getNamespace()Ljava/lang/String;")
    @Definition(id = "equals", method = "Ljava/lang/String;equals(Ljava/lang/Object;)Z")
    @Expression("resourceLocation.getNamespace().equals('minecraft')")
    @ModifyExpressionValue(method = "filterResources(Ljava/lang/Iterable;Ljava/lang/String;Ljava/util/function/Function;Ljava/util/function/Consumer;)V", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static boolean corelibs$alwaysAllowSuggestion(boolean original) {
        return true;
    }
}