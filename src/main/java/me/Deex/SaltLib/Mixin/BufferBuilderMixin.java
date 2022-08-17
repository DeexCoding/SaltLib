package me.Deex.SaltLib.Mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormatElement;

@Mixin(BufferBuilder.class)
public class BufferBuilderMixin 
{
    @Shadow
    private VertexFormat format;

    @Shadow
    private VertexFormatElement currentElement;

    @Shadow
    private int currentElementId;

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    private void nextElement() //Basically gets the next element in the buffer
    {
        List<VertexFormatElement> elements = format.getElements();

        do
        {
            this.currentElementId++;
            if (this.currentElementId >= elements.size()) 
            {
                this.currentElementId -= elements.size();
            }

            currentElement = elements.get(currentElementId);

        } while (currentElement.getType() == VertexFormatElement.Type.PADDING);
    }
}
