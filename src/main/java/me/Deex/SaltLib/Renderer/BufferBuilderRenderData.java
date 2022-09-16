package me.Deex.SaltLib.Renderer;

import java.nio.ByteBuffer;

import me.Deex.SaltLib.Utils.Util;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormat;

public class BufferBuilderRenderData 
{
    public ByteBuffer byteBuffer;
    public VertexFormat format;
    public int vertexCount;
    public int drawMode;

    public BufferBuilderRenderData(BufferBuilder builder)
    {
        SetData(builder);
    }

    public void SetData(BufferBuilder builder)
    {
        byteBuffer = ByteBuffer.allocateDirect(builder.getByteBuffer().capacity());
        Util.CopyBuffer(builder.getByteBuffer(), byteBuffer);
        format = new VertexFormat(builder.getFormat());
        vertexCount = builder.getVertexCount();
        drawMode = builder.getDrawMode();
    }
}
