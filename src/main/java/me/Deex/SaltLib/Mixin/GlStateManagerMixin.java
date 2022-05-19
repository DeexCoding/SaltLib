package me.Deex.SaltLib.Mixin;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import com.mojang.blaze3d.platform.GlStateManager;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import me.Deex.SaltLib.Renderer.MatrixStack;
import me.Deex.SaltLib.Utils.MatrixUtil;

@Mixin(GlStateManager.class)
public class GlStateManagerMixin 
{
    private static MatrixStack currentMatrixStack;

    @Inject(method = "matrixMode", at = @At("HEAD"))
    private static void matrixMode(int mode) 
    {
        currentMatrixStack = MatrixStack.GetGLStack(mode);
    }

    @Inject(method = "loadIdentity", at = @At("HEAD"))
    private static void loadIdentity() 
    {
        currentMatrixStack.SetIdentity();
    }

    @Inject(method = "pushMatrix", at = @At("HEAD"))
    private static void pushMatrix() 
    {
        currentMatrixStack.Push();
    }

    @Inject(method = "popMatrix", at = @At("HEAD"))
    private static void popMatrix() 
    {
        currentMatrixStack.Pop();
    }

    @Overwrite
    public static void getFloat(int mode, FloatBuffer buffer) 
    {
        FloatBuffer glMatAsBuffer = ByteBuffer.allocateDirect(16 * Float.BYTES).asFloatBuffer();
        GL11.glGetFloat(mode, glMatAsBuffer);

        Matrix4f mat = new Matrix4f();
        mat.load(glMatAsBuffer);
        Matrix4f.mul(mat, MatrixStack.GetGLStack(mode).GetTop(), mat);

        MatrixUtil.Matrix4fToFloatBuffer(mat, buffer);
    }

}
