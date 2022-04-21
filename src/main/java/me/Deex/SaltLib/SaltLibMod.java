package me.Deex.SaltLib;

import net.fabricmc.api.ClientModInitializer;

public class SaltLibMod implements ClientModInitializer 
{
	

	@Override
	public void onInitializeClient() 
	{
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("SaltLib initalized");
	}
}
