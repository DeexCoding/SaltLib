package me.Deex.SaltLib.Renderer;

import java.util.List;

import com.google.common.collect.Lists;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class MatrixStack 
{
    private static MatrixStack modelview;
    private static MatrixStack projection;
    private static MatrixStack texture;

    public static void CreateGLStacks()
    {
        modelview = new MatrixStack();
        projection = new MatrixStack();
        texture = new MatrixStack();
    }

    public static MatrixStack GetGLStack(int glId)
    {
        switch (glId)
        {
            case GL11.GL_MODELVIEW:
            {
                return modelview;
            }

            case GL11.GL_PROJECTION:
            {
                return projection;
            }

            case GL11.GL_TEXTURE:
            {
                return texture;
            }

            case GL11.GL_MODELVIEW_MATRIX:
            {
                return modelview;
            }

            case GL11.GL_PROJECTION_MATRIX:
            {
                return projection;
            }

            case GL11.GL_TEXTURE_MATRIX:
            {
                return texture;
            }
        }

        throw new IllegalStateException("Invalid [glId] in [MatrixStack.Get]!");
    }

    private List<Matrix4f> stack;

    MatrixStack()
    {
        stack = Lists.newArrayList();
        stack.add(0, new Matrix4f());
    }

    public Matrix4f GetTop()
    {
        return stack.get(0);
    }

    public void SetIdentity()
    {
        stack.set(0, new Matrix4f());
    }

    public void Push()
    {
        Matrix4f newMat = new Matrix4f(stack.get(0));
        stack.add(0, newMat);
    }
    
    public void Pop()
    {
        stack.remove(0);
    }

    public void Ortho(float l, float r, float b, float t, float n, float f)
    {
        Matrix4f orthoMat = new Matrix4f();
        orthoMat.m00 = 2.0f / (r - l);
        orthoMat.m11 = 2.0f / (t - b);
        orthoMat.m22 = -2.0f / (f - n); //Could do 2.0f / (n - f)

        orthoMat.m30 = -((r + l) / (r - l));
        orthoMat.m31 = -((t + b) / (t - b));
        orthoMat.m32 = -((f + n) / (f - n));

        Matrix4f.mul(stack.get(0), orthoMat, stack.get(0));
    }

    public void Rotate(float angle, Vector3f axis)
    {
        Matrix4f.rotate((float)Math.toRadians((float)angle), (Vector3f)axis.normalise(), stack.get(0), stack.get(0));
    }

    public void Scale(Vector3f scale)
    {
        stack.get(0).scale(scale);
    }

    public void Translate(Vector3f translate)
    {
        stack.get(0).translate(translate);
    }

    public void Multiply(Matrix4f mat)
    {
        Matrix4f.mul(stack.get(0), mat, stack.get(0));
    }
}