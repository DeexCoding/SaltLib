package me.Deex.SaltLib.Renderer;

import java.nio.ByteBuffer;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GLX;

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
        
        int stride = vertexFormat.getVertexSize();
        List<VertexFormatElement> list = vertexFormat.getElements();

        for (int i = 0; i < list.size(); ++i) 
        {
            VertexFormatElement vertexFormatElement = list.get(i);
            VertexFormatElement.Type type = vertexFormatElement.getType();
            int vertexTypeID = vertexFormatElement.getFormat().getGlId();
            byteBuffer.position(vertexFormat.getIndex(i));

            switch (type)
            {
                case POSITION: 
                {
                    GL11.glVertexPointer(vertexFormatElement.getCount(), vertexTypeID, stride, byteBuffer);
                    GL11.glEnableClientState(32884);
                    break;
                }
                case UV: 
                {
                    GLX.gl13ClientActiveTexture(GLX.textureUnit + vertexFormatElement.getIndex());
                    GL11.glTexCoordPointer(vertexFormatElement.getCount(), vertexTypeID, stride, byteBuffer);
                    GL11.glEnableClientState(32888);
                    GLX.gl13ClientActiveTexture(GLX.textureUnit);
                    break;
                }
                case COLOR: 
                {
                    GL11.glColorPointer(vertexFormatElement.getCount(), vertexTypeID, stride, byteBuffer);
                    GL11.glEnableClientState(32886);
                    break;
                }
                case NORMAL: 
                {
                    GL11.glNormalPointer(vertexTypeID, stride, byteBuffer);
                    GL11.glEnableClientState(32885);
                    break;
                }
                case BLEND_WEIGHT:
                case MATRIX:
                case PADDING:
                break;
            }
        }

        GL11.glDrawArrays(drawMode, 0, vertexCount);

        //No need to unbind textures, clear colors, they will get set once we need them
        //NOTE: Altough we need to disable coloring, texturing and everything else, because the next Draw won't disable it for itself
        //We technically don't need to disable positions, because we can't render anyting without a position
        //GL11.glDisableClientState(32884);
        GL11.glDisableClientState(32888);
        GL11.glDisableClientState(32886);
        GL11.glDisableClientState(32885);
    }

    public static void DrawNoReset(BufferBuilder builder)
    {
        InternalDraw(builder.getByteBuffer(), builder.getFormat(), builder.getVertexCount(), builder.getDrawMode());
    }

    public static void DrawNoReset(BufferBuilderRenderData builder)
    {
        InternalDraw(builder.byteBuffer, builder.format, builder.vertexCount, builder.drawMode);
    }

    public static void DrawAndReset(BufferBuilder builder)
    {
        DrawNoReset(builder);
        builder.reset();
    }

}
