package me.Deex.SaltLib.Mixin;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexBuffer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin 
{
    @Shadow
    private void method_9922() {}

    @Shadow
    @Final
    private MinecraftClient client;
    
    @Shadow
    private ClientWorld world;
    
    @Shadow
    private boolean field_10817;

    @Shadow
    private VertexBuffer field_10826;

    @Shadow
    private int field_1924 = -1;
    
    @Shadow
    @Final
    private static Identifier SUN;

    @Shadow
    @Final
    private TextureManager textureManager;

    @Shadow
    @Final
    private static Identifier MOON_PHASES;

    @Shadow
    private VertexBuffer field_10827;
    
    @Shadow
    private VertexBuffer starsBuffer;

    @Shadow
    private int field_1925;
    
    @Shadow
    private int field_1923;

    @Overwrite
    public void method_9891(float f, int i) {
        float w;
        float v;
        int u;
        int t;
        float s;
        float o;
        float n;
        if (this.client.world.dimension.getType() == 1) {
            this.method_9922();
            return;
        }
        if (!this.client.world.dimension.canPlayersSleep()) {
            return;
        }
        GlStateManager.disableTexture();
        Vec3d vec3d = this.world.method_3631(this.client.getCameraEntity(), f);
        float g = (float)vec3d.x;
        float h = (float)vec3d.y;
        float j = (float)vec3d.z;
        if (i != 2) {
            float k = (g * 30.0f + h * 59.0f + j * 11.0f) / 100.0f;
            float l = (g * 30.0f + h * 70.0f) / 100.0f;
            float m = (g * 30.0f + j * 70.0f) / 100.0f;
            g = k;
            h = l;
            j = m;
        }
        GlStateManager.color3f(g, h, j);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.depthMask(false);
        GlStateManager.enableFog();
        GlStateManager.color3f(g, h, j);
        if (this.field_10817) {
            this.field_10826.bind();
            GL11.glEnableClientState(32884);
            GL11.glVertexPointer(3, 5126, 12, 0L);
            this.field_10826.draw(7);
            this.field_10826.unbind();
            GL11.glDisableClientState(32884);
        } else {
            GlStateManager.callList(this.field_1924);
        }
        GlStateManager.disableFog();
        GlStateManager.disableAlphaTest();
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
        DiffuseLighting.disable();
        float[] fs = this.world.dimension.getBackgroundColor(this.world.getSkyAngle(f), f);
        if (fs != null) {
            GlStateManager.disableTexture();
            GlStateManager.shadeModel(7425);
            GlStateManager.pushMatrix();
            GlStateManager.rotatef(90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotatef(MathHelper.sin(this.world.getSkyAngleRadians(f)) < 0.0f ? 180.0f : 0.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotatef(90.0f, 0.0f, 0.0f, 1.0f);
            n = fs[0];
            o = fs[1];
            float p = fs[2];
            if (i != 2) {
                float q = (n * 30.0f + o * 59.0f + p * 11.0f) / 100.0f;
                float r = (n * 30.0f + o * 70.0f) / 100.0f;
                s = (n * 30.0f + p * 70.0f) / 100.0f;
                n = q;
                o = r;
                p = s;
            }
            bufferBuilder.begin(6, VertexFormats.POSITION_COLOR);
            bufferBuilder.vertex(0.0, 100.0, 0.0).color(n, o, p, fs[3]).next();
            t = 16;
            for (u = 0; u <= 16; ++u) {
                s = (float)u * (float)Math.PI * 2.0f / 16.0f;
                v = MathHelper.sin(s);
                w = MathHelper.cos(s);
                bufferBuilder.vertex(v * 120.0f, w * 120.0f, -w * 40.0f * fs[3]).color(fs[0], fs[1], fs[2], 0.0f).next();
            }
            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.shadeModel(7424);
        }
        GlStateManager.enableTexture();
        GlStateManager.blendFuncSeparate(770, 1, 1, 0);
        GlStateManager.pushMatrix();
        n = 1.0f - this.world.getRainGradient(f);
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, n);
        GlStateManager.rotatef(-90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotatef(this.world.getSkyAngle(f) * 360.0f, 1.0f, 0.0f, 0.0f);
        o = 30.0f;
        this.textureManager.bindTexture(SUN);
        bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(-o, 100.0, -o).texture(0.0, 0.0).next();
        bufferBuilder.vertex(o, 100.0, -o).texture(1.0, 0.0).next();
        bufferBuilder.vertex(o, 100.0, o).texture(1.0, 1.0).next();
        bufferBuilder.vertex(-o, 100.0, o).texture(0.0, 1.0).next();
        tessellator.draw();
        o = 20.0f;
        this.textureManager.bindTexture(MOON_PHASES);
        int x = this.world.getMoonPhase();
        t = x % 4;
        u = x / 4 % 2;
        s = (float)(t + 0) / 4.0f;
        v = (float)(u + 0) / 2.0f;
        w = (float)(t + 1) / 4.0f;
        float y = (float)(u + 1) / 2.0f;
        bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(-o, -100.0, o).texture(w, y).next();
        bufferBuilder.vertex(o, -100.0, o).texture(s, y).next();
        bufferBuilder.vertex(o, -100.0, -o).texture(s, v).next();
        bufferBuilder.vertex(-o, -100.0, -o).texture(w, v).next();
        tessellator.draw();
        GlStateManager.disableTexture();
        float z = this.world.method_3707(f) * n;
        if (z > 0.0f) {
            GlStateManager.color4f(z, z, z, z);
            if (this.field_10817) {
                this.starsBuffer.bind();
                GL11.glEnableClientState(32884);
                GL11.glVertexPointer(3, 5126, 12, 0L);
                this.starsBuffer.draw(7);
                this.starsBuffer.unbind();
                GL11.glDisableClientState(32884);
            } else {
                GlStateManager.callList(this.field_1923);
            }
        }
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.enableFog();
        GlStateManager.popMatrix();
        GlStateManager.disableTexture();
        GlStateManager.color3f(0.0f, 0.0f, 0.0f);
        double d = this.client.player.getCameraPosVec((float)f).y - this.world.getHorizonHeight();
        if (d < 0.0) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0.0f, 12.0f, 0.0f);
            if (this.field_10817) {
                this.field_10827.bind();
                GL11.glEnableClientState(32884);
                GL11.glVertexPointer(3, 5126, 12, 0L);
                this.field_10827.draw(7);
                this.field_10827.unbind();
                GL11.glDisableClientState(32884);
            } else {
                GlStateManager.callList(this.field_1925);
            }
            GlStateManager.popMatrix();
            float p = 1.0f;
            float q = -((float)(d + 65.0));
            float r = -1.0f;
            s = q;
            bufferBuilder.begin(7, VertexFormats.POSITION_COLOR);
            bufferBuilder.vertex(-1.0, s, 1.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(1.0, s, 1.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(1.0, -1.0, 1.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(-1.0, -1.0, 1.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(-1.0, -1.0, -1.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(1.0, -1.0, -1.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(1.0, s, -1.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(-1.0, s, -1.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(1.0, -1.0, -1.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(1.0, -1.0, 1.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(1.0, s, 1.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(1.0, s, -1.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(-1.0, s, -1.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(-1.0, s, 1.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(-1.0, -1.0, 1.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(-1.0, -1.0, -1.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(-1.0, -1.0, -1.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(-1.0, -1.0, 1.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(1.0, -1.0, 1.0).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(1.0, -1.0, -1.0).color(0, 0, 0, 255).next();
            tessellator.draw();
        }
        if (this.world.dimension.hasGround()) {
            GlStateManager.color3f(g * 0.2f + 0.04f, h * 0.2f + 0.04f, j * 0.6f + 0.1f);
        } else {
            GlStateManager.color3f(g, h, j);
        }
        GlStateManager.pushMatrix();
        GlStateManager.translatef(0.0f, -((float)(d - 16.0)), 0.0f);
        GlStateManager.callList(this.field_1925);
        GlStateManager.popMatrix();
        GlStateManager.enableTexture();
        GlStateManager.depthMask(true);
    }
    
}
