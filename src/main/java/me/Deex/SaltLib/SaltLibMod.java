package me.Deex.SaltLib;

import me.Deex.SaltLib.Debug.Instrumentor;
import me.Deex.SaltLib.Renderer.GLShader;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class SaltLibMod implements ClientModInitializer 
{
	public static boolean enableProfiling;
	public static GLShader defaultShader;

	@Override
	public void onInitializeClient() 
	{
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		OnStartup();

		System.out.println("[SaltLib] Initalized successfully");
	}

	private static void OnStartup()
	{
		enableProfiling = FabricLoader.getInstance().isDevelopmentEnvironment();
		
		if (enableProfiling)
		{
			Instrumentor.BeginSession("SaltLib-profile");
		}
	}

	public static void OnShutdown()
	{
		if (enableProfiling)
		{
			Instrumentor.EndSession();
		}
	}
}
