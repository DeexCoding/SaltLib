package me.Deex.SaltLib.Renderer;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL20;

import me.Deex.SaltLib.SaltLibMod;

public class GLShader 
{
    public int glProgram;

    public GLShader(String vertexShaderPath, String fragmentShaderPath)
    {
        byte[] vshBytes = new byte[4096];
        byte[] fshBytes = new byte[4096];

        try
        {
            //TODO: Slow, but it's only load code, so it's not suuuuper important
            SaltLibMod.class.getResourceAsStream(vertexShaderPath).read(vshBytes, 0, 4096);
            SaltLibMod.class.getResourceAsStream(fragmentShaderPath).read(fshBytes, 0, 4096);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        int vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
        buffer.put(vshBytes);
        buffer.position(0);
        GL20.glShaderSource(vertexShader, buffer);
        GL20.glCompileShader(vertexShader);
        
        int success = GL20.glGetShaderi(vertexShader, GL20.GL_COMPILE_STATUS);
        if (success == 0)
        {
            String infoLog = GL20.glGetShaderInfoLog(vertexShader, 512);
            System.err.println("Could not compile vertex shader: " + infoLog);
        }
        
        int fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        buffer = ByteBuffer.allocateDirect(4096);
        buffer.put(fshBytes);
        buffer.position(0);
        GL20.glShaderSource(fragmentShader, buffer);
        GL20.glCompileShader(fragmentShader);

        success = GL20.glGetShaderi(fragmentShader, GL20.GL_COMPILE_STATUS);
        if (success == 0)
        {
            String infoLog = GL20.glGetShaderInfoLog(fragmentShader, 512);
            System.err.println("Could not compile fragment shader: " + infoLog);
        }

        glProgram = GL20.glCreateProgram();
        GL20.glAttachShader(glProgram, vertexShader);
        GL20.glAttachShader(glProgram, fragmentShader);
        GL20.glLinkProgram(glProgram);

        success = GL20.glGetProgrami(glProgram, GL20.GL_LINK_STATUS);
        if (success == 0)
        {
            String infoLog = GL20.glGetProgramInfoLog(glProgram, 512);
            System.err.println("Could not link shader: " + infoLog);
        }

        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);
    }
}