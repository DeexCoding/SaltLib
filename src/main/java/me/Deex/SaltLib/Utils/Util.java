package me.Deex.SaltLib.Utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

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
}
