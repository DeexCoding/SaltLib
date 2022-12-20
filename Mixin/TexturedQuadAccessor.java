package me.Deex.SaltLib.Mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.util.TexturedQuad;
import net.minecraft.client.util.math.TexturePosition;

@Mixin(TexturedQuad.class)
public interface TexturedQuadAccessor 
{
    @Accessor
    TexturePosition[] getPositions();

    @Accessor
    boolean getField_1507();
}
