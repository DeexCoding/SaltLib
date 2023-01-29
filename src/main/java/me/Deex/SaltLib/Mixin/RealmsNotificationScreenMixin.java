package me.Deex.SaltLib.Mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;

@Mixin(RealmsNotificationsScreen.class)
public class RealmsNotificationScreenMixin 
{
    //Stub function to remove random crash when starting up.
    //I'm 99% sure it's a problem with fabric.
    @Overwrite
    public void init() 
    {
    }
}
