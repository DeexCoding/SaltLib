package me.Deex.SaltLib.Renderer;

import java.nio.ByteBuffer;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import com.mojang.blaze3d.platform.GLX;

import me.Deex.SaltLib.SaltLibMod;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormatElement;

public class CustomBufferRenderer
{
    public static int vao;
    public static int vbo;

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
                    int startPos = byteBuffer.position();

                    float x = 0.0f;
                    float y = 0.0f;
                    float z = 0.0f;

                    for (int v = 0; v < vertexCount; ++v)
                    {
                        byteBuffer.position(startPos + v * stride);

                        switch(vertexFormatElement.getFormat())
                        {
                        case UNSIGNED_INT:
                        case INT:
                        case FLOAT:
                        {
                            x = byteBuffer.getFloat();
                            y = byteBuffer.getFloat();
                            z = byteBuffer.getFloat();
                        } break;
                        
                        case UNSIGNED_SHORT: 
                        case SHORT:
                        {
                            x = (float)byteBuffer.getShort();
                            y = (float)byteBuffer.getShort();
                            z = (float)byteBuffer.getShort();
                        } break;
                        
                        case UNSIGNED_BYTE: 
                        case BYTE:
                        {
                            x = (float)byteBuffer.get();
                            y = (float)byteBuffer.get();
                            z = (float)byteBuffer.get();
                        } break;
                        }

                        Vector4f point = new Vector4f(x, y, z, 1.0f);
                        point = Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_MODELVIEW).GetTop(), point, point);
                        point = Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_PROJECTION).GetTop(), point, point);
                        x = point.x / point.w;
                        y = point.y / point.w;
                        z = point.z / point.w;

                        byteBuffer.position(startPos + v * stride);
                        byteBuffer.putFloat(x);
                        byteBuffer.putFloat(y);
                        byteBuffer.putFloat(z);
                    }

                    byteBuffer.position(startPos);

                    GL11.glVertexPointer(vertexFormatElement.getCount(), vertexTypeID, stride, byteBuffer);
                    GL11.glEnableClientState(32884);
                    break;
                }
                case UV: 
                {
                    int startPos = byteBuffer.position();

                    float x = 0.0f;
                    float y = 0.0f;

                    for (int v = 0; v < vertexCount; ++v)
                    {
                        byteBuffer.position(startPos + v * stride);

                        x = byteBuffer.getFloat();
                        y = byteBuffer.getFloat();

                        Vector4f point = new Vector4f(x, y, 0.0f, 1.0f);
                        point = Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_TEXTURE).GetTop(), point, point);
                        x = point.x / point.w;
                        y = point.y / point.w;

                        byteBuffer.position(startPos + v * stride);
                        byteBuffer.putFloat(x);
                        byteBuffer.putFloat(y);
                    }

                    byteBuffer.position(startPos);

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

        //GL20.glUseProgram(SaltLibMod.defaultShader.glProgram);
        //GL30.glBindVertexArray(vao);
        GL11.glDrawArrays(drawMode, 0, vertexCount);
        //GL30.glBindVertexArray(0);
        //GL20.glUseProgram(0);

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
