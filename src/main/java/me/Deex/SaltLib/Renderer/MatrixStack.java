package me.Deex.SaltLib.Renderer;

import java.util.List;

import com.google.common.collect.Lists;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class MatrixStack 
{
    private static int currentStackId;

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
            case GL11.GL_MODELVIEW_MATRIX:
            case GL11.GL_MODELVIEW:
            {
                return modelview;
            }

            case GL11.GL_PROJECTION_MATRIX:
            case GL11.GL_PROJECTION:
            {
                return projection;
            }

            case GL11.GL_TEXTURE_MATRIX:
            case GL11.GL_TEXTURE:
            {
                return texture;
            }
        }

        throw new IllegalStateException("Invalid [glId] in [MatrixStack.GetGLStack]!");
    }

    public static void TransformWorldPoint(Vector3f point)
    {
        Vector4f pos = new Vector4f(point.x, point.y, point.z, 1.0f);
        pos = Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_MODELVIEW).GetTop(), pos, pos);
        pos = Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_PROJECTION).GetTop(), pos, pos);
        point.x = pos.x / pos.w;
        point.y = pos.y / pos.w;
        point.z = pos.z / pos.w;
    }

    public static void SetCurrentStack(int id)
    {
        currentStackId = id;
    }

    public static MatrixStack GetCurrentStack()
    {
        return GetGLStack(currentStackId);
    }

    private List<Matrix4f> stack;

    MatrixStack()
    {
        stack = Lists.newArrayList();
        stack.add(new Matrix4f());
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

    public void Perspective(float fovy, float aspect, float zNear, float zFar)
    {
        float sine, cotangent, deltaZ;
		float radians = fovy / 2 * (float)Math.PI / 180;

		deltaZ = zFar - zNear;
		sine = (float) Math.sin(radians);

		if ((deltaZ == 0) || (sine == 0) || (aspect == 0)) {
			return;
		}

		cotangent = (float) Math.cos(radians) / sine;

        Matrix4f mat = new Matrix4f();

        mat.m00 = cotangent / aspect;
        mat.m11 = cotangent;
        mat.m22 = - (zFar + zNear) / deltaZ;
        mat.m23 = -1;
        mat.m32 = -2 * zNear * zFar / deltaZ;
        mat.m33 = 0;

        Matrix4f.mul(stack.get(0), mat, stack.get(0));
    }

    public void Rotate(float angle, Vector3f axis)
    {
        Matrix4f.rotate((float)Math.toRadians((double)angle), (Vector3f)axis.normalise(), stack.get(0), stack.get(0));
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