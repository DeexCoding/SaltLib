package me.Deex.SaltLib.Mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin 
{
    @Inject(at = @At("TAIL"), method = "initializeGame")
    private void initializeGame(CallbackInfo ci)
    {
        //This will make startup slower lol
        MinecraftClient.getInstance().options.vbo = true;
        MinecraftClient.getInstance().options.save();
        MinecraftClient.getInstance().worldRenderer.reload();
    }
}
