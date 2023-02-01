package me.Deex.SaltLib.Mixin;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;

import me.Deex.SaltLib.Renderer.MatrixStack;
import me.Deex.SaltLib.Utils.Util;
import net.minecraft.client.util.GlAllocationUtils;
import net.minecraft.client.util.math.Matrix4f;

@Mixin(GlStateManager.class)
public class GlStateManagerMixin 
{
    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void matrixMode(int mode)
    {
        MatrixStack.SetCurrentStack(mode);
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void loadIdentity() 
    {
        MatrixStack.GetCurrentStack().SetIdentity();
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void pushMatrix() 
    {
        MatrixStack.GetCurrentStack().Push();
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void popMatrix() 
    {
        MatrixStack.GetCurrentStack().Pop();
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Inject(at = @At("HEAD"), method = "getFloat", cancellable = true)
    private static void getFloat(int mode, FloatBuffer buffer, CallbackInfo ci) 
    {
        switch (mode)
        {
            case GL11.GL_MODELVIEW:
            case GL11.GL_PROJECTION:
            case GL11.GL_TEXTURE:
            case GL11.GL_MODELVIEW_MATRIX:
            case GL11.GL_PROJECTION_MATRIX:
            case GL11.GL_TEXTURE_MATRIX:
            {
                int startPos = buffer.position();
                
                Util.WriteMatrix4fIntoFloatBuffer(MatrixStack.GetGLStack(mode).GetTop(), buffer);

                buffer.position(startPos);
                ci.cancel();
            }
        }

    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void ortho(double l, double r, double b, double t, double n, double f) 
    {
        MatrixStack.GetCurrentStack().Ortho((float)l, (float)r, (float)b, (float)t, (float)n, (float)f);
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void scalef(float x, float y, float z) 
    {
        MatrixStack.GetCurrentStack().Scale(new Vector3f(x, y, z));
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void scaled(double x, double y, double z) 
    {
        MatrixStack.GetCurrentStack().Scale(new Vector3f((float)x, (float)y, (float)z));
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void rotatef(float angle, float x, float y, float z) 
    {
        MatrixStack.GetCurrentStack().Rotate(angle, new Vector3f(x, y, z));
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void translatef(float x, float y, float z) 
    {
        MatrixStack.GetCurrentStack().Translate(new Vector3f(x, y, z));
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void translated(double x, double y, double z) 
    {
        MatrixStack.GetCurrentStack().Translate(new Vector3f((float)x, (float)y, (float)z));
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
        int startPos = buffer.position();
        mat.load(buffer);
        buffer.position(startPos);
        MatrixStack.GetCurrentStack().Multiply(mat);
    }
}
