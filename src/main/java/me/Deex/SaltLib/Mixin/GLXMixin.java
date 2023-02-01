package me.Deex.SaltLib.Mixin;

import com.mojang.blaze3d.platform.GLX;

import me.Deex.SaltLib.SaltLibMod;
import me.Deex.SaltLib.Renderer.CustomBufferRenderer;
import me.Deex.SaltLib.Renderer.GLShader;
import me.Deex.SaltLib.Renderer.MatrixStack;
import net.fabricmc.loader.api.FabricLoader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.KHRDebugCallback;
import org.lwjgl.util.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GLX.class)
public class GLXMixin 
{
	@Inject(method = "createContext", at = @At("TAIL"))
	private static void createContext(CallbackInfo ci) 
	{
		//NOTE: Ground work for switching to VAOs from old vbo-only rendering

		if (FabricLoader.getInstance().isDevelopmentEnvironment())
		{
			//GL11.glEnable(GL43.GL_DEBUG_OUTPUT);
			//GL43.glDebugMessageCallback(new KHRDebugCallback());
		}

		SaltLibMod.defaultShader = new GLShader("/assets/saltlib/DefaultVertexShader.glsl", 
			"/assets/saltlib/DefaultFragmentShader.glsl");

		GL20.glUseProgram(0);

		CustomBufferRenderer.vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(CustomBufferRenderer.vao);

		CustomBufferRenderer.vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, CustomBufferRenderer.vbo);
		
		GL30.glBindVertexArray(0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		MatrixStack.CreateGLStacks();
		//MatrixStack.GetGLStack(GL11.GL_PROJECTION).Translate(new Vector3f(1000000.0f, 0.0f, 0.0f));
		
		System.out.println("[SaltLib] Initalized OpenGL stuff");
	}
}
// 