package me.Deex.SaltLib.Mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;


import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.world.BuiltChunk;

@Mixin(ChunkBuilder.class)
public class ChunkBuilderMixin 
{
    @Overwrite
    private void method_10125(BufferBuilder bufferBuilder, int i, BuiltChunk builtChunk)
    {
        //NOTE: To my knowledge, this function doesn't get called when VBOs are turned on.
        return;
    }
}
