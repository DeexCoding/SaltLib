package me.Deex.SaltLib.Renderer;

import java.nio.ByteBuffer;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormat;

public class GhostBufferBuilder 
{
    public ByteBuffer byteBuffer;
    public VertexFormat format;
    public int vertexCount;
    public int drawMode;

    public GhostBufferBuilder(BufferBuilder builder)
    {
        byteBuffer = builder.getByteBuffer().duplicate();
        format = new VertexFormat(builder.getFormat());
        vertexCount = builder.getVertexCount();
        drawMode = builder.getDrawMode();
    }
}
