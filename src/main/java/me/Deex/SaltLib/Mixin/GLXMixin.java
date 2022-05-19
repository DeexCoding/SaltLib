package me.Deex.SaltLib.Mixin;

import com.mojang.blaze3d.platform.GLX;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.Deex.SaltLib.Renderer.MatrixStack;

@Mixin(GLX.class)
public class GLXMixin 
{
    @Inject(method = "createContext", at = @At("TAIL"))
    private static void createContext(CallbackInfo ci) 
    {
        MatrixStack.CreateStacks();
    }
}
