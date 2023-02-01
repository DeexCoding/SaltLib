package me.Deex.SaltLib.Mixin;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.blaze3d.platform.GlStateManager;

import me.Deex.SaltLib.Renderer.MatrixStack;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

@Mixin(TextRenderer.class)
public class TextRendererMixin 
{
    @Shadow
    private int[] characterWidths;

    @Shadow
    private byte[] glyphWidths;

    @Shadow
    @Final
    private Identifier fontTexture;

    @Shadow
    @Final
    private TextureManager textureManager;

    @Shadow
    private float x;

    @Shadow
    private float y;

    //TODO: Speed this up with vaos and batching!!!!!!!!!!!!!!!!!!

    @Overwrite
    private float drawLayerNormal(int characterIndex, boolean italic)
    {
        Vector3f pos;

        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.pushMatrix();

        int i = characterIndex % 16 * 8;
        int j = characterIndex / 16 * 8;
        float k = italic ? 1.0f : 0.0f;
        this.textureManager.bindTexture(this.fontTexture);
        int l = this.characterWidths[characterIndex];
        float f = (float)l - 0.01f;
        GL11.glBegin(5);

        GL11.glTexCoord2f((float)i / 128.0f, (float)j / 128.0f);
        pos = new Vector3f(this.x + k, this.y, 0.0f);
        MatrixStack.TransformWorldPoint(pos);
        GL11.glVertex3f(pos.x, pos.y, pos.z);

        GL11.glTexCoord2f((float)i / 128.0f, ((float)j + 7.99f) / 128.0f);
        pos = new Vector3f(this.x - k, this.y + 7.99f, 0.0f);
        MatrixStack.TransformWorldPoint(pos);
        GL11.glVertex3f(pos.x, pos.y, pos.z);

        GL11.glTexCoord2f(((float)i + f - 1.0f) / 128.0f, (float)j / 128.0f);
        pos = new Vector3f(this.x + f - 1.0f + k, this.y, 0.0f);
        MatrixStack.TransformWorldPoint(pos);
        GL11.glVertex3f(pos.x, pos.y, pos.z);

        GL11.glTexCoord2f(((float)i + f - 1.0f) / 128.0f, ((float)j + 7.99f) / 128.0f);
        pos = new Vector3f(this.x + (float)f - 1.0f - k, this.y + 7.99f, 0.0f);
        MatrixStack.TransformWorldPoint(pos);
        GL11.glVertex3f(pos.x, pos.y, pos.z);

        GL11.glEnd();
        
        GlStateManager.popMatrix();

        return l;
    }
    
    @Overwrite
    private float drawLayerUnicode(char character, boolean italic) 
    {
        if (this.glyphWidths[character] == 0) {
            return 0.0f;
        }
        int i = character / 256;
        this.bindPageTexture(i);
        int j = this.glyphWidths[character] >>> 4;
        int k = this.glyphWidths[character] & 0xF;
        float f = j;
        float g = k + 1;
        float h = (float)(character % 16 * 16) + f;
        float l = (character & 0xFF) / 16 * 16;
        float m = g - f - 0.02f;
        float n = italic ? 1.0f : 0.0f;
        GL11.glBegin(5);
        GL11.glTexCoord2f(h / 256.0f, l / 256.0f);
        GL11.glVertex3f(this.x + n, this.y, 0.0f);
        GL11.glTexCoord2f(h / 256.0f, (l + 15.98f) / 256.0f);
        GL11.glVertex3f(this.x - n, this.y + 7.99f, 0.0f);
        GL11.glTexCoord2f((h + m) / 256.0f, l / 256.0f);
        GL11.glVertex3f(this.x + m / 2.0f + n, this.y, 0.0f);
        GL11.glTexCoord2f((h + m) / 256.0f, (l + 15.98f) / 256.0f);
        GL11.glVertex3f(this.x + m / 2.0f - n, this.y + 7.99f, 0.0f);
        GL11.glEnd();
        return (g - f) / 2.0f + 1.0f;
    }
    
    @Shadow
    private void bindPageTexture(int index) {}
}
