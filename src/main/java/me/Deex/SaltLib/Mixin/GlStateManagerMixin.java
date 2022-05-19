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
    private static MatrixStack currentMatrixStack;

    @Inject(method = "matrixMode", at = @At("HEAD"))
    private static void matrixMode(int mode, CallbackInfo ci) 
    {
        currentMatrixStack = MatrixStack.GetGLStack(mode);
    }

    @Inject(method = "loadIdentity", at = @At("HEAD"))
    private static void loadIdentity(CallbackInfo ci) 
    {
        currentMatrixStack.SetIdentity();
    }

    @Inject(method = "pushMatrix", at = @At("HEAD"))
    private static void pushMatrix(CallbackInfo ci) 
    {
        currentMatrixStack.Push();
    }

    @Inject(method = "popMatrix", at = @At("HEAD"))
    private static void popMatrix(CallbackInfo ci) 
    {
        currentMatrixStack.Pop();
    }

    @Overwrite
    public static void translatef(float x, float y, float z) 
    {
        currentMatrixStack.Translate(new Vector3f(x, y, z));
    }

    @Overwrite
    public static void translated(double x, double y, double z) 
    {
        currentMatrixStack.Translate(new Vector3f((float)x, (float)y, (float)z));
    }

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

}
