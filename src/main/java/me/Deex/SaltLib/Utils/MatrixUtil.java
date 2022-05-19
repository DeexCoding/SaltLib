package me.Deex.SaltLib.Utils;

import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Matrix4f;

public class MatrixUtil 
{
    public static void Matrix4fToFloatBuffer(Matrix4f mat, FloatBuffer buffer)
    {
		if (buffer.remaining() < 16 * Float.BYTES)
		{
			throw new IllegalStateException("Not enough bytes remaining in buffer!");
		}

		buffer.put(mat.m00);
		buffer.put(mat.m01);
		buffer.put(mat.m02);
		buffer.put(mat.m03);
		buffer.put(mat.m10);
		buffer.put(mat.m11);
		buffer.put(mat.m12);
		buffer.put(mat.m13);
		buffer.put(mat.m20);
		buffer.put(mat.m21);
		buffer.put(mat.m22);
		buffer.put(mat.m23);
		buffer.put(mat.m30);
		buffer.put(mat.m31);
		buffer.put(mat.m32);
		buffer.put(mat.m33);
    }
}
