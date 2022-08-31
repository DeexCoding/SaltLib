package me.Deex.SaltLib.Utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Matrix4f;

import net.minecraft.client.render.BufferBuilder;

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

	public static void CopyBufferBuilder(BufferBuilder bufferBuilder)
	{
		
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

	public static boolean Matrix4fEquals(Matrix4f a, Matrix4f b)
	{
		if (a.m00 == b.m00 && a.m01 == b.m01 && a.m02 == b.m02 && a.m03 == b.m03)
		{
			if (a.m10 == b.m10 && a.m11 == b.m11 && a.m12 == b.m12 && a.m13 == b.m13)
			{
				if (a.m20 == b.m20 && a.m21 == b.m21 && a.m22 == b.m22 && a.m23 == b.m23)
				{
					if (a.m30 == b.m30 && a.m31 == b.m31 && a.m32 == b.m32 && a.m33 == b.m33)
					{
						return true;
					}
				}
			}
		}

		return false;
	}
}
