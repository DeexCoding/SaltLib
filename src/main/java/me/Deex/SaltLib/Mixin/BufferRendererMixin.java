package me.Deex.SaltLib.Mixin;

import me.Deex.SaltLib.Renderer.CustomBufferRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;

@Mixin(BufferRenderer.class)
public class BufferRendererMixin 
{
    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public void draw(BufferBuilder builder) 
    {
        CustomBufferRenderer.DrawAndReset(builder);
    }
}
