package me.Deex.SaltLib.Mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.world.ChunkRenderHelperImpl;
import net.minecraft.client.world.BuiltChunk;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ChunkRenderHelperImpl.class)
public abstract class ChunkRenderHelperImplMixin extends BuiltChunk
{
    @Redirect(method="<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/GlAllocationUtils;genLists(I)I"))
    public static synchronized int redirect(int i) 
    {
        return -1;
    }

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
