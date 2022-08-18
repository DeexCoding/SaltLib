package me.Deex.SaltLib.Mixin;

import java.nio.FloatBuffer;

import com.mojang.blaze3d.platform.GlStateManager;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.Deex.SaltLib.Renderer.MatrixStack;
import me.Deex.SaltLib.Utils.Util;

import net.minecraft.client.util.GlAllocationUtils;

@Mixin(GlStateManager.class)
public class GlStateManagerMixin 
{
    private static int currentMatrixStack;

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Inject(at = @At("HEAD"), method = "matrixMode", cancellable = true)
    private static void matrixMode(int mode, CallbackInfo ci)
    {
        currentMatrixStack = mode;
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Inject(at = @At("HEAD"), method = "loadIdentity", cancellable = true)
    private static void loadIdentity(CallbackInfo ci) 
    {
        MatrixStack.GetGLStack(currentMatrixStack).SetIdentity();
        //ci.cancel();
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Inject(at = @At("HEAD"), method = "pushMatrix", cancellable = true)
    private static void pushMatrix(CallbackInfo ci) 
    {
        MatrixStack.GetGLStack(currentMatrixStack).Push();
        //ci.cancel();
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Inject(at = @At("HEAD"), method = "popMatrix", cancellable = true)
    private static void popMatrix(CallbackInfo ci) 
    {
        MatrixStack.GetGLStack(currentMatrixStack).Pop();
        //ci.cancel();
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
            case GL11.GL_MODELVIEW_MATRIX:
            case GL11.GL_PROJECTION_MATRIX:
            case GL11.GL_TEXTURE_MATRIX:
            {
                int startPos = buffer.position();
                
                FloatBuffer glMatAsBuffer = GlAllocationUtils.allocateFloatBuffer(16);
                GL11.glGetFloat(mode, glMatAsBuffer);
        
                Matrix4f mat = new Matrix4f();
                //Matrix4f mat = MatrixStack.GetGLStack(mode).GetTop();
                mat.load(glMatAsBuffer);
                Matrix4f.mul( MatrixStack.GetGLStack(mode).GetTop(), mat, mat);
                Util.WriteMatrix4fIntoFloatBuffer(mat, buffer);
        

                buffer.position(startPos);
                //ci.cancel();
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
        MatrixStack.GetGLStack(currentMatrixStack).Ortho((float)l, (float)r, (float)b, (float)t, (float)n, (float)f);
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void scalef(float x, float y, float z) 
    {
        MatrixStack.GetGLStack(currentMatrixStack).Scale(new Vector3f(x, y, z));
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void scaled(double x, double y, double z) 
    {
        MatrixStack.GetGLStack(currentMatrixStack).Scale(new Vector3f((float)x, (float)y, (float)z));
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void rotatef(float angle, float x, float y, float z) 
    {
        MatrixStack.GetGLStack(currentMatrixStack).Rotate(angle, new Vector3f(x, y, z));
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void translatef(float x, float y, float z) 
    {
        MatrixStack.GetGLStack(currentMatrixStack).Translate(new Vector3f(x, y, z));
    }

    /**
     * what is javadoc
     * @author me
     * @reason mod needs functionality
     */
    @Overwrite
    public static void translated(double x, double y, double z) 
    {
        MatrixStack.GetGLStack(currentMatrixStack).Translate(new Vector3f((float)x, (float)y, (float)z));
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
        MatrixStack.GetGLStack(currentMatrixStack).Multiply(mat);
    }
    /**/
}
