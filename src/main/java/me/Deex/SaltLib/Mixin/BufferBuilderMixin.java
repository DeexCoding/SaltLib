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
    private VertexFormat field_10653; //The format of the vertex

    @Shadow
    private VertexFormatElement element; //The current element more specifically

    @Shadow
    private int field_10648; //The element'sid

    @Overwrite
    private void method_9758() //Basically gets the next element in the buffer
    {
        List<VertexFormatElement> elements = field_10653.getElements();

        do
        {
            this.field_10648++;
            if (this.field_10648 >= elements.size()) 
            {
                this.field_10648 -= elements.size();
            }

            element = elements.get(field_10648);

        } while (element.getType() == VertexFormatElement.Type.PADDING);
    }
}
