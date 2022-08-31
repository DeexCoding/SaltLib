package me.Deex.SaltLib.Mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.render.ModelBox;
import net.minecraft.client.util.TexturedQuad;

@Mixin(ModelBox.class)
public interface ModelBoxAccessor 
{
    @Accessor
    TexturedQuad[] getQuads();
}
