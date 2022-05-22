package me.Deex.SaltLib.Mixin;

import java.nio.FloatBuffer;

import com.mojang.blaze3d.platform.GlStateManager;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.Deex.SaltLib.Renderer.MatrixStack;
import me.Deex.SaltLib.Utils.Util;

import net.minecraft.client.util.GlAllocationUtils;

@Mixin(GlStateManager.class)
public class GlStateManagerMixin 
{
    private static MatrixStack currentMatrixStack;

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void matrixMode(int mode) 
    {
        currentMatrixStack = MatrixStack.GetGLStack(mode);
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void loadIdentity() 
    {
        currentMatrixStack.SetIdentity();
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void pushMatrix() 
    {
        currentMatrixStack.Push();
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void popMatrix() 
    {
        currentMatrixStack.Pop();
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void getFloat(int mode, FloatBuffer buffer) 
    {
        int startPos = buffer.position();
        
        FloatBuffer glMatAsBuffer = GlAllocationUtils.allocateFloatBuffer(16);
        GL11.glGetFloat(mode, glMatAsBuffer);

        Matrix4f mat = new Matrix4f();
        mat.load(glMatAsBuffer);
        Matrix4f.mul(mat, currentMatrixStack.GetTop(), mat);
        Util.WriteMatrix4fIntoFloatBuffer(mat, buffer);

        buffer.position(startPos);
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void ortho(double l, double r, double b, double t, double n, double f) 
    {
        currentMatrixStack.Ortho((float)l, (float)r, (float)b, (float)t, (float)n, (float)f);
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void scalef(float x, float y, float z) 
    {
        currentMatrixStack.Scale(new Vector3f(x, y, z));
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void scaled(double x, double y, double z) 
    {
        currentMatrixStack.Scale(new Vector3f((float)x, (float)y, (float)z));
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void rotatef(float angle, float x, float y, float z) 
    {
        currentMatrixStack.Rotate(angle, new Vector3f(x, y, z));
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void translatef(float x, float y, float z) 
    {
        currentMatrixStack.Translate(new Vector3f(x, y, z));
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void translated(double x, double y, double z) 
    {
        currentMatrixStack.Translate(new Vector3f((float)x, (float)y, (float)z));
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void multiMatrix(FloatBuffer buffer) 
    {
        Matrix4f mat = new Matrix4f();
        mat.load(buffer);
        currentMatrixStack.Multiply(mat);
    }
}
