package me.Deex.SaltLib.Renderer;

import java.nio.ByteBuffer;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormatElement;

public class CustomBufferRenderer 
{
    private static void InternalDraw(ByteBuffer byteBuffer, VertexFormat vertexFormat, int vertexCount, int drawMode)
    {
        if (vertexCount <= 0) 
        {
            return;
        }

        int textureUnitOffset;
        int j;
        int stride = vertexFormat.getVertexSize();
        List<VertexFormatElement> list = vertexFormat.getElements();
        int listSize = list.size();
        block12: for (j = 0; j < listSize; ++j) 
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
        GL11.glDrawArrays(drawMode, 0, vertexCount);

        //No need to unbind textures, clear colors, they will get set once we need them
        //Altough we need to disable coloring, texturing and everything else, because the next Draw won't disable it for itself
        //NOTE: We technically don't need to disable positions, because we can't render anyting without a position
        //GL11.glDisableClientState(32884);
        GL11.glDisableClientState(32888);
        GL11.glDisableClientState(32886);
        GL11.glDisableClientState(32885);
        
    }

    public static void DrawNoReset(BufferBuilder builder)
    {
        InternalDraw(builder.getByteBuffer(), builder.getFormat(), builder.getVertexCount(), builder.getDrawMode());
    }

    public static void DrawNoReset(GhostBufferBuilder builder)
    {
        InternalDraw(builder.byteBuffer, builder.format, builder.vertexCount, builder.drawMode);
    }

    public static void DrawAndReset(BufferBuilder builder)
    {
        DrawNoReset(builder);
        builder.reset();
    }

}
