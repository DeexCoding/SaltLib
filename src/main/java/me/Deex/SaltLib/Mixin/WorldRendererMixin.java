package me.Deex.SaltLib.Mixin;

import java.util.Random;

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
import net.minecraft.client.render.VertexFormat;
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
    private void renderEndSky() {}

    @Shadow
    @Final
    private MinecraftClient client;
    
    @Shadow
    private ClientWorld world;
    
    @Shadow
    private boolean vbo;

    @Shadow
    private VertexBuffer lightSkyBuffer;

    @Shadow
    private int lightSkyList = -1;
    
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
    private VertexBuffer darkSkyBuffer;
    
    @Shadow
    private VertexBuffer starsBuffer;

    @Shadow
    private int darkSkyList;
    
    @Shadow
    private int starsList; //Render command list for stars
    
    @Shadow
    private VertexFormat skyVertexFormat;

    @Overwrite
    public void renderSky(float f, int i) 
    {
        float w;
        float v;
        int u;
        int t;
        float s;
        float o;
        float n;
        if (this.client.world.dimension.getType() == 1) 
        {
            this.renderEndSky();
            return;
        }
        if (!this.client.world.dimension.canPlayersSleep()) 
        {
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
        GlStateManager.color3f(g, h, j); //Not sure
        if (this.vbo) 
        {
            this.lightSkyBuffer.bind();
            //GL11.glEnableClientState(32884); //Position
            GL11.glVertexPointer(3, 5126, 12, 0L);
            this.lightSkyBuffer.draw(7);
            this.lightSkyBuffer.unbind();
            //GL11.glDisableClientState(32884); //Position
        } 
        else 
        {
            //GlStateManager.callList(this.lightSkyList);
            CallListField1924();
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
        if (z > 0.0f) 
        {
            GlStateManager.color4f(z, z, z, z);
            if (this.vbo) 
            {
                this.starsBuffer.bind();
                //GL11.glEnableClientState(32884); //Position
                GL11.glVertexPointer(3, 5126, 12, 0L);
                this.starsBuffer.draw(7);
                this.starsBuffer.unbind();
                //GL11.glDisableClientState(32884); //Position
            } 
            else 
            {
                //GlStateManager.callList(this.starsList);
                CallListField1923();
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
        if (d < 0.0) 
        {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0.0f, 12.0f, 0.0f);
            if (this.vbo) 
            {
                this.darkSkyBuffer.bind();
                //GL11.glEnableClientState(32884); //Position
                GL11.glVertexPointer(3, 5126, 12, 0L);
                this.darkSkyBuffer.draw(7);
                this.darkSkyBuffer.unbind();
                //GL11.glDisableClientState(32884); //Position
            } 
            else 
            {
                //GlStateManager.callList(this.darkSkyList);
                CallListField1925();
            }
            GlStateManager.popMatrix();
            //float p = 1.0f;
            float q = -((float)(d + 65.0));
            //float r = -1.0f;
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
        //GlStateManager.callList(this.darkSkyList);
        CallListField1925();
        GlStateManager.popMatrix();
        GlStateManager.enableTexture();
        GlStateManager.depthMask(true);

    }

    @Shadow
    private void renderDarkSky() 
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        if (this.darkSkyBuffer == null) 
        {
            this.darkSkyBuffer = new VertexBuffer(this.skyVertexFormat);
        }
        if (this.darkSkyList >= 0) 
        {
            //GlAllocationUtils.deleteSingletonList(this.darkSkyList);
            this.darkSkyList = -1;
        }
        if (this.vbo) 
        {
            this.renderSkyHalf(bufferBuilder, -16.0f, true);
            bufferBuilder.end();
            bufferBuilder.reset();
            this.darkSkyBuffer.data(bufferBuilder.getByteBuffer());
        } else {
            this.darkSkyList = 3;
            //GL11.glNewList(this.darkSkyList, 4864); //GL_COMPILE
            //this.renderSkyHalf(bufferBuilder, -16.0f, true); //Doesn't contain gl calls?
            //tessellator.draw();
            //GL11.glEndList();
        }

    }

    private void CallListField1925()
    {
        Tessellator tessellator = Tessellator.getInstance();
        //BufferBuilder bufferBuilder = tessellator.getBuffer();
        //this.renderSkyHalf(bufferBuilder, -16.0f, true); //Doesn't contain gl calls?
        tessellator.draw();
    }

    @Overwrite
    private void renderLightSky() 
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        if (this.lightSkyBuffer == null) 
        {
            this.lightSkyBuffer = new VertexBuffer(this.skyVertexFormat);
        }
        if (this.lightSkyList >= 0) 
        {
            //GlAllocationUtils.deleteSingletonList(this.lightSkyList);
            this.lightSkyList = -1;
        }
        if (this.vbo) 
        {
            this.renderSkyHalf(bufferBuilder, 16.0f, false);
            bufferBuilder.end();
            bufferBuilder.reset();
            this.lightSkyBuffer.data(bufferBuilder.getByteBuffer());
        } else 
        {
            this.lightSkyList = 2;
            //GL11.glNewList(this.lightSkyList, 4864); //GL_COMPILE
            //this.renderSkyHalf(bufferBuilder, 16.0f, false); //Doesn't contain gl calls?
            //tessellator.draw();
            //GL11.glEndList();
        }
    }
    
    private void CallListField1924()
    {
        Tessellator tessellator = Tessellator.getInstance();
        //BufferBuilder bufferBuilder = tessellator.getBuffer();
        //this.renderSkyHalf(bufferBuilder, 16.0f, false); //Doesn't contain gl calls?
        tessellator.draw();
    }

    @Shadow
    private void renderStars() 
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        if (this.starsBuffer == null)
        {
            this.starsBuffer = new VertexBuffer(this.skyVertexFormat);
        }
        if (this.starsList >= 0) 
        {
            //GlAllocationUtils.deleteSingletonList(this.starsList);
            this.starsList = -1;
        }
        if (this.vbo) 
        {
            this.renderStars(bufferBuilder);
            bufferBuilder.end();
            bufferBuilder.reset();
            this.starsBuffer.data(bufferBuilder.getByteBuffer());
        } 
        else 
        {
            this.starsList = 5;
            //GlStateManager.pushMatrix();
            //GL11.glNewList(this.starsList, 4864); //GL_COMPILE
            //this.renderStars(bufferBuilder); //Doesn't contain gl calls?
            //tessellator.draw();
            //GL11.glEndList();
            //GlStateManager.popMatrix();
        }

    }

    private void CallListField1923()
    {
        Tessellator tessellator = Tessellator.getInstance();
        //this.renderStars(bufferBuilder); //Doesn't contain gl calls?
        tessellator.draw();
    }

    @Overwrite
    private void renderStars(BufferBuilder buffer) 
    {
        Random random = new Random(10842L);
        buffer.begin(7, VertexFormats.POSITION);
        for (int i = 0; i < 1500; ++i) {
            double d = random.nextFloat() * 2.0f - 1.0f;
            double e = random.nextFloat() * 2.0f - 1.0f;
            double f = random.nextFloat() * 2.0f - 1.0f;
            double g = 0.15f + random.nextFloat() * 0.1f;
            double h = d * d + e * e + f * f;
            if (!(h < 1.0) || !(h > 0.01)) continue;
            h = 1.0 / Math.sqrt(h);
            double j = (d *= h) * 100.0;
            double k = (e *= h) * 100.0;
            double l = (f *= h) * 100.0;
            double m = Math.atan2(d, f);
            double n = Math.sin(m);
            double o = Math.cos(m);
            double p = Math.atan2(Math.sqrt(d * d + f * f), e);
            double q = Math.sin(p);
            double r = Math.cos(p);
            double s = random.nextDouble() * Math.PI * 2.0;
            double t = Math.sin(s);
            double u = Math.cos(s);
            for (int v = 0; v < 4; ++v) 
            {
                double x = (double)((v & 2) - 1) * g;
                double y = (double)((v + 1 & 2) - 1) * g;
                double aa = x * u - y * t;
                double ac = y * u + x * t;
                double ad = aa * q + 0.0 * r;
                double ae = 0.0 * q - aa * r;
                double af = ae * n - ac * o;
                double ag = ad;
                double ah = ac * n + ae * o;
                buffer.vertex(j + af, k + ag, l + ah).next();
            }
        }
    }

    @Shadow
    private void renderSkyHalf(BufferBuilder buffer, float y, boolean bottom) {}
}
