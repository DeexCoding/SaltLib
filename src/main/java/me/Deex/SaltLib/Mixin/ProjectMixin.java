package me.Deex.SaltLib.Mixin;

import org.lwjgl.util.glu.Project;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import me.Deex.SaltLib.Renderer.MatrixStack;

@Mixin(Project.class)
public class ProjectMixin 
{	
	@Overwrite(remap = false)
    public static void gluPerspective(float fovy, float aspect, float zNear, float zFar) 
    {
        MatrixStack.GetCurrentStack().Perspective(fovy, aspect, zNear, zFar);
	}
}
