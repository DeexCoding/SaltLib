package me.Deex.SaltLib.Mixin;

import java.util.List;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import me.Deex.SaltLib.Renderer.MatrixStack;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormatElement;

@Mixin(BufferBuilder.class)
public class BufferBuilderMixin 
{
    @Shadow
    private ByteBuffer buffer;

    @Shadow
    private VertexFormat format;

    @Shadow
    private VertexFormatElement currentElement;

    @Shadow
    private int currentElementId;
    
    @Shadow
    private int vertexCount;
    
    @Shadow
    private double offsetX;

    @Shadow
    private double offsetY;

    @Shadow
    private double offsetZ;

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

    
    //The matrix tranforms are not exact functionaltiy, since OpenGL transforms the verticies with the matrix stack once 
    //it is requested to be drawn or glBegin has been called

    @Overwrite
    public BufferBuilder vertex(double x, double y, double z) 
    {
        int i = this.vertexCount * this.format.getVertexSize() + this.format.getIndex(this.currentElementId);

        Vector4f vec = new Vector4f((float)x, (float)y, (float)z, 1.0f);

        //(m1*m2)*v is the same as m1*(m2*v)
        //Where m1 and m2 are matrices and v is a vector
        //projection * view -> view first thenprojection

        Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_MODELVIEW).GetTop(), vec, vec);
        Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_PROJECTION).GetTop(), vec, vec);

        x = (double)vec.x;
        y = (double)vec.y;
        z = (double)vec.z;

        switch (this.currentElement.getFormat()) 
        {
            case FLOAT: 
            {
                this.buffer.putFloat(i, (float)(x + this.offsetX));
                this.buffer.putFloat(i + 4, (float)(y + this.offsetY));
                this.buffer.putFloat(i + 8, (float)(z + this.offsetZ));
                break;
            }
            case UNSIGNED_INT: 
            case INT: 
            {
                this.buffer.putInt(i, Float.floatToRawIntBits((float)(x + this.offsetX)));
                this.buffer.putInt(i + 4, Float.floatToRawIntBits((float)(y + this.offsetY)));
                this.buffer.putInt(i + 8, Float.floatToRawIntBits((float)(z + this.offsetZ)));
                break;
            }
            case UNSIGNED_SHORT: 
            case SHORT: 
            {
                this.buffer.putShort(i, (short)(x + this.offsetX));
                this.buffer.putShort(i + 2, (short)(y + this.offsetY));
                this.buffer.putShort(i + 4, (short)(z + this.offsetZ));
                break;
            }
            case UNSIGNED_BYTE: 
            case BYTE: 
            {
                this.buffer.put(i, (byte)(x + this.offsetX));
                this.buffer.put(i + 1, (byte)(y + this.offsetY));
                this.buffer.put(i + 2, (byte)(z + this.offsetZ));
            }
        }
        this.nextElement();
        return (BufferBuilder)(Object)this;
    }
    
    public BufferBuilder texture(double u, double v) 
    {
        int i = this.vertexCount * this.format.getVertexSize() + this.format.getIndex(this.currentElementId);

        Vector4f vec = new Vector4f((float)u, (float)v, 1.0f, 1.0f);

        Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_TEXTURE).GetTop(), vec, vec);

        u = (float)vec.x;
        v = (float)vec.y;

        switch (this.currentElement.getFormat()) 
        {
            case FLOAT: 
            {
                this.buffer.putFloat(i, (float)u);
                this.buffer.putFloat(i + 4, (float)v);
                break;
            }
            case UNSIGNED_INT: 
            case INT: 
            {
                this.buffer.putInt(i, (int)u);
                this.buffer.putInt(i + 4, (int)v);
                break;
            }
            case UNSIGNED_SHORT: 
            case SHORT: 
            {
                this.buffer.putShort(i, (short)v);
                this.buffer.putShort(i + 2, (short)u);
                break;
            }
            case UNSIGNED_BYTE: 
            case BYTE: 
            {
                this.buffer.put(i, (byte)v);
                this.buffer.put(i + 1, (byte)u);
            }
        }
        this.nextElement();
        return (BufferBuilder)(Object)this;
    }

    public BufferBuilder texture2(int u, int v) 
    {
        //TODO: Call to texture instead

        int i = this.vertexCount * this.format.getVertexSize() + this.format.getIndex(this.currentElementId);
        
        Vector4f vec = new Vector4f((float)u, (float)v, 1.0f, 1.0f);

        Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_TEXTURE).GetTop(), vec, vec);

        u = (int)vec.x;
        v = (int)vec.y;

        switch (this.currentElement.getFormat()) 
        {
            case FLOAT: 
            {
                this.buffer.putFloat(i, u);
                this.buffer.putFloat(i + 4, v);
                break;
            }
            case UNSIGNED_INT: 
            case INT: 
            {
                this.buffer.putInt(i, u);
                this.buffer.putInt(i + 4, v);
                break;
            }
            case UNSIGNED_SHORT: 
            case SHORT: 
            {
                this.buffer.putShort(i, (short)v);
                this.buffer.putShort(i + 2, (short)u);
                break;
            }
            case UNSIGNED_BYTE: 
            case BYTE: 
            {
                this.buffer.put(i, (byte)v);
                this.buffer.put(i + 1, (byte)u);
            }
        }
        this.nextElement();
        return (BufferBuilder)(Object)this;
    }
}
