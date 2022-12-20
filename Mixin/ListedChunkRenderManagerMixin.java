package me.Deex.SaltLib.Mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.world.AbstractChunkRenderManager;
import net.minecraft.client.render.world.ListedChunkRenderManager;

@Mixin(ListedChunkRenderManager.class)
public class ListedChunkRenderManagerMixin extends AbstractChunkRenderManager
{
    @Override
    public void render(RenderLayer layer)
    {
        //NOTE: To my knowledge, this function doesn't get called when VBOs are turned on.
        return;
    }
}
