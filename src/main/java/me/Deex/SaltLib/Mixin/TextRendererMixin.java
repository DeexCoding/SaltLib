package me.Deex.SaltLib.Mixin;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import me.Deex.SaltLib.Renderer.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.texture.TextureManager;

@Mixin(TextRenderer.class)
public class TextRendererMixin 
{
    @Shadow
    private int[] field_1143;

    @Shadow
    private byte[] field_1144;

    @Shadow
    @Final
    private Identifier fontTexture;
    
    @Shadow
    @Final
    private TextureManager textureManager;

    @Shadow
    private float field_1149;
    
    @Shadow
    private float field_1150;


    @Overwrite
    private float method_953(int i, boolean bl) 
    {
        int j = i % 16 * 8;
        int k = i / 16 * 8;
        float l = bl ? 1.0f : 0.0f;
        this.textureManager.bindTexture(this.fontTexture);
        int m = this.field_1143[i];
        float f = (float)m - 0.01f;

                        //Will move back over to GPU once shaders will get implemented
        Vector4f v1 = new Vector4f(this.field_1149 + (float)l, this.field_1150, 0.0f, 0.0f);
        Vector4f v2 = new Vector4f(this.field_1149 - (float)l, this.field_1150 + 7.99f, 0.0f, 0.0f);
        Vector4f v3 = new Vector4f(this.field_1149 + f - 1.0f + (float)l, this.field_1150, 0.0f, 0.0f);
        Vector4f v4 = new Vector4f(this.field_1149 + f - 1.0f - (float)l, this.field_1150 + 7.99f, 0.0f, 0.0f);

        Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_MODELVIEW_MATRIX).GetTop(), v1, v1);
        Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_PROJECTION_MATRIX).GetTop(), v1, v1);
        Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_MODELVIEW_MATRIX).GetTop(), v2, v2);
        Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_PROJECTION_MATRIX).GetTop(), v2, v2);
        Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_MODELVIEW_MATRIX).GetTop(), v3, v3);
        Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_PROJECTION_MATRIX).GetTop(), v3, v3);
        Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_MODELVIEW_MATRIX).GetTop(), v4, v4);
        Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_PROJECTION_MATRIX).GetTop(), v4, v4);

        GL11.glBegin(5);
        GL11.glTexCoord2f((float)j / 128.0f, (float)k / 128.0f);
        GL11.glVertex3f(v1.x, v1.y, v1.z);
        GL11.glTexCoord2f((float)j / 128.0f, ((float)k + 7.99f) / 128.0f);
        GL11.glVertex3f(v2.x, v2.y, v2.z);
        GL11.glTexCoord2f(((float)j + f - 1.0f) / 128.0f, (float)k / 128.0f);
        GL11.glVertex3f(v3.x, v3.y, v3.z);
        GL11.glTexCoord2f(((float)j + f - 1.0f) / 128.0f, ((float)k + 7.99f) / 128.0f);
        GL11.glVertex3f(v4.x, v4.y, v4.z);
        GL11.glEnd();
        return m;
    }

    @Overwrite
    private float method_950(char c, boolean bl) 
    {
        if (this.field_1144[c] == 0) {
            return 0.0f;
        }
        int i = c / 256;
        this.method_951(i);
        int j = this.field_1144[c] >>> 4;
        int k = this.field_1144[c] & 0xF;
        float f = j;
        float g = k + 1;
        float h = (float)(c % 16 * 16) + f;
        float l = (c & 0xFF) / 16 * 16;
        float m = g - f - 0.02f;
        float n = bl ? 1.0f : 0.0f;

                        //Will move back over to GPU once shaders will get implemented
        Vector4f v1 = new Vector4f(this.field_1149 + n, this.field_1150, 0.0f, 0.0f);
        Vector4f v2 = new Vector4f(this.field_1149 - n, this.field_1150 + 7.99f, 0.0f, 0.0f);
        Vector4f v3 = new Vector4f(this.field_1149 + m / 2.0f + n, this.field_1150, 0.0f, 0.0f);
        Vector4f v4 = new Vector4f(this.field_1149 + m / 2.0f - n, this.field_1150 + 7.99f, 0.0f, 0.0f);

        Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_MODELVIEW_MATRIX).GetTop(), v1, v1);
        Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_PROJECTION_MATRIX).GetTop(), v1, v1);
        Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_MODELVIEW_MATRIX).GetTop(), v2, v2);
        Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_PROJECTION_MATRIX).GetTop(), v2, v2);
        Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_MODELVIEW_MATRIX).GetTop(), v3, v3);
        Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_PROJECTION_MATRIX).GetTop(), v3, v3);
        Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_MODELVIEW_MATRIX).GetTop(), v4, v4);
        Matrix4f.transform(MatrixStack.GetGLStack(GL11.GL_PROJECTION_MATRIX).GetTop(), v4, v4);

        GL11.glBegin(5);
        GL11.glTexCoord2f(h / 256.0f, l / 256.0f);
        GL11.glVertex3f(v1.x, v1.y, v1.z);
        GL11.glTexCoord2f(h / 256.0f, (l + 15.98f) / 256.0f);
        GL11.glVertex3f(v2.x, v2.y, v2.z);
        GL11.glTexCoord2f((h + m) / 256.0f, l / 256.0f);
        GL11.glVertex3f(v3.x, v3.y, v3.z);
        GL11.glTexCoord2f((h + m) / 256.0f, (l + 15.98f) / 256.0f);
        GL11.glVertex3f(v4.x, v4.y, v4.z);
        GL11.glEnd();
        return (g - f) / 2.0f + 1.0f;
    
    }

    @Shadow
    private void method_951(int i) {};
}
