package me.Deex.SaltLib.Utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Matrix4f;

public class Util 
{
	public static void CopyBuffer(ByteBuffer from, ByteBuffer to)
	{
		from.position(0);

		while (from.remaining() > 0)
		{
			to.put(from.get());
		}
	}

	public static void CopyBuffer(FloatBuffer from, FloatBuffer to)
	{
		from.position(0);

		while (from.remaining() > 0)
		{
			to.put(from.get());
		}
	}

	
    public static void WriteMatrix4fIntoFloatBuffer(Matrix4f mat, FloatBuffer buffer)
    {
		if (buffer.remaining() < 16)
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
