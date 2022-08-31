package me.Deex.SaltLib.Mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.world.ChunkRenderHelperImpl;
import net.minecraft.client.util.GlAllocationUtils;
import net.minecraft.client.world.BuiltChunk;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ChunkRenderHelperImpl.class)
public abstract class ChunkRenderHelperImplMixin extends BuiltChunk
{
    //TODO: Ask someone smart what to do with the constructor when mixing in with an extends class
    public ChunkRenderHelperImplMixin(World world, WorldRenderer worldRenderer, BlockPos blockPos, int i) 
    {
        super(world, worldRenderer, blockPos, i);
    }

    @Override
    public void delete() 
    {
        return;
    }
}
