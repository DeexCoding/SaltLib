package me.Deex.SaltLib.Mixin;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormatElement;

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
        if (builder.getVertexCount() <= 0) 
        {
            return;
        }

        int textureUnitOffset;
        int j;
        VertexFormat vertexFormat = builder.getFormat();
        int stride = vertexFormat.getVertexSize();
        ByteBuffer byteBuffer = builder.getByteBuffer();
        List<VertexFormatElement> list = vertexFormat.getElements();
        block12: for (j = 0; j < list.size(); ++j) 
        {
            VertexFormatElement vertexFormatElement = list.get(j);
            VertexFormatElement.Type type = vertexFormatElement.getType();
            int vertexTypeID = vertexFormatElement.getFormat().getGlId();
            textureUnitOffset = vertexFormatElement.getIndex();
            byteBuffer.position(vertexFormat.getIndex(j));
            switch (type) 
            {
                case POSITION: 
                {
                    GL11.glVertexPointer(vertexFormatElement.getCount(), vertexTypeID, stride, byteBuffer);
                    GL11.glEnableClientState(32884);
                    continue block12;
                }
                case UV: 
                {
                    GLX.gl13ClientActiveTexture(GLX.textureUnit + textureUnitOffset);
                    GL11.glTexCoordPointer(vertexFormatElement.getCount(), vertexTypeID, stride, byteBuffer);
                    GL11.glEnableClientState(32888);
                    GLX.gl13ClientActiveTexture(GLX.textureUnit);
                    continue block12;
                }
                case COLOR: 
                {
                    GL11.glColorPointer(vertexFormatElement.getCount(), vertexTypeID, stride, byteBuffer);
                    GL11.glEnableClientState(32886);
                    continue block12;
                }
                case NORMAL: 
                {
                    GL11.glNormalPointer(vertexTypeID, stride, byteBuffer);
                    GL11.glEnableClientState(32885);
                }
            }
        }
        GL11.glDrawArrays(builder.getDrawMode(), 0, builder.getVertexCount());
        int m = list.size();
        block13: for (j = 0; j < m; ++j) 
        {
            VertexFormatElement vertexFormatElement2 = list.get(j);
            VertexFormatElement.Type type2 = vertexFormatElement2.getType();
            textureUnitOffset = vertexFormatElement2.getIndex();
            switch (type2) 
            {
                case POSITION: 
                {
                    GL11.glDisableClientState(32884);
                    continue block13;
                }
                case UV: 
                {
                    GLX.gl13ClientActiveTexture(GLX.textureUnit + textureUnitOffset);
                    GL11.glDisableClientState(32888);
                    GLX.gl13ClientActiveTexture(GLX.textureUnit);
                    continue block13;
                }
                case COLOR: 
                {
                    GL11.glDisableClientState(32886);
                    GlStateManager.clearColor();
                    continue block13;
                }
                case NORMAL: 
                {
                    GL11.glDisableClientState(32885);
                }
            }
        }

        builder.reset();
    }
}
