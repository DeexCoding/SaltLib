package me.Deex.SaltLib.Mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.world.BuiltChunk;

@Mixin(ChunkBuilder.class)
public class ChunkBuilderMixin 
{
    @Shadow
    @Final
    private BufferRenderer field_11040;

    /*@Overwrite
    private void method_10125(BufferBuilder bufferBuilder, int i, BuiltChunk builtChunk)
    {
        GlStateManager.pushMatrix();
        builtChunk.method_10169();
        this.field_11040.draw(bufferBuilder);
        GlStateManager.popMatrix();
    }
    /* */
}
