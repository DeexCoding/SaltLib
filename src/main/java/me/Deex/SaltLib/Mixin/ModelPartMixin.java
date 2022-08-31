package me.Deex.SaltLib.Mixin;

import java.nio.ByteBuffer;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;

import me.Deex.SaltLib.Renderer.CustomBufferRenderer;
import me.Deex.SaltLib.Renderer.GhostBufferBuilder;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.ModelBox;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.model.ModelPart;
import net.minecraft.client.util.TexturedQuad;
import net.minecraft.client.util.math.TexturePosition;
import net.minecraft.util.math.Vec3d;

@Mixin(ModelPart.class)
public class ModelPartMixin 
{
    private GhostBufferBuilder ghostBufferBuilder;
    private BufferBuilder customBufferBuilder = new BufferBuilder(1024);

    @Shadow
    private boolean field_1611;

    @Shadow
    private int field_1612;

    @Shadow
    public List<ModelBox> cuboids;
    
    @Shadow
    public List<ModelPart> modelList;

    @Shadow
    public boolean field_1605;
    
    @Shadow
    public boolean visible;

    @Shadow
    public float pivotX;

    @Shadow
    public float pivotY;

    @Shadow
    public float pivotZ;

    @Shadow
    public float posX;

    @Shadow
    public float posY;

    @Shadow
    public float posZ;

    @Shadow
    public float field_5142;

    @Shadow
    public float field_5143;

    @Shadow
    public float field_5144;

    public void render(float scale) 
    {
        if (this.field_1605) 
        {
            return;
        }
        if (!this.visible) 
        {
            return;
        }
        if (!this.field_1611) 
        {
            this.method_1196(scale);
        }
        GlStateManager.translatef(this.field_5142, this.field_5143, this.field_5144);
        if (this.posX != 0.0f || this.posY != 0.0f || this.posZ != 0.0f) 
        {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(this.pivotX * scale, this.pivotY * scale, this.pivotZ * scale);
            if (this.posZ != 0.0f) 
            {
                GlStateManager.rotatef(this.posZ * 57.295776f, 0.0f, 0.0f, 1.0f);
            }
            if (this.posY != 0.0f) 
            {
                GlStateManager.rotatef(this.posY * 57.295776f, 0.0f, 1.0f, 0.0f);
            }
            if (this.posX != 0.0f) 
            {
                GlStateManager.rotatef(this.posX * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            //GlStateManager.callList(this.field_1612);
            CallListField1612();
            if (this.modelList != null) 
            {
                for (int i = 0; i < this.modelList.size(); ++i) 
                {
                    this.modelList.get(i).render(scale);
                }
            }
            GlStateManager.popMatrix();
        } 
        else if (this.pivotX != 0.0f || this.pivotY != 0.0f || this.pivotZ != 0.0f) 
        {
            GlStateManager.translatef(this.pivotX * scale, this.pivotY * scale, this.pivotZ * scale);
            //GlStateManager.callList(this.field_1612);
            CallListField1612();
            if (this.modelList != null) 
            {
                for (int i = 0; i < this.modelList.size(); ++i) 
                {
                    this.modelList.get(i).render(scale);
                }
            }
            GlStateManager.translatef(-this.pivotX * scale, -this.pivotY * scale, -this.pivotZ * scale);
        } 
        else 
        {
            //GlStateManager.callList(this.field_1612);
            CallListField1612();
            if (this.modelList != null) 
            {
                for (int i = 0; i < this.modelList.size(); ++i) 
                {
                    this.modelList.get(i).render(scale);
                }
            }
        }
        GlStateManager.translatef(-this.field_5142, -this.field_5143, -this.field_5144);
    }

    public void method_1193(float f) 
    {
        if (this.field_1605) 
        {
            return;
        }
        if (!this.visible) 
        {
            return;
        }
        if (!this.field_1611) 
        {
            this.method_1196(f);
        }
        GlStateManager.pushMatrix();
        GlStateManager.translatef(this.pivotX * f, this.pivotY * f, this.pivotZ * f);
        if (this.posY != 0.0f) 
        {
            GlStateManager.rotatef(this.posY * 57.295776f, 0.0f, 1.0f, 0.0f);
        }
        if (this.posX != 0.0f) 
        {
            GlStateManager.rotatef(this.posX * 57.295776f, 1.0f, 0.0f, 0.0f);
        }
        if (this.posZ != 0.0f) 
        {
            GlStateManager.rotatef(this.posZ * 57.295776f, 0.0f, 0.0f, 1.0f);
        }
        //GlStateManager.callList(this.field_1612);
        CallListField1612();
        GlStateManager.popMatrix();
    }

    @Overwrite
    private void method_1196(float scale) 
    {
        this.field_1612 = 4;

        //GL11.glNewList(this.field_1612, 4864); //GL_COMPILE
        /*BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(7, VertexFormats.ENTITY);
        for (int i = 0; i < this.cuboids.size(); ++i) 
        {
            ModelBox cubiod = this.cuboids.get(i);

            for (int i2 = 0; i2 < ((ModelBoxAccessor)cubiod).getQuads().length; ++i2) 
            {
                TexturedQuad quad = ((ModelBoxAccessor)cubiod).getQuads()[i2];
                
                Vec3d vec3d = ((TexturedQuadAccessor)quad).getPositions()[1].position.reverseSubtract(((TexturedQuadAccessor)quad).getPositions()[0].position);
                Vec3d vec3d2 = ((TexturedQuadAccessor)quad).getPositions()[1].position.reverseSubtract(((TexturedQuadAccessor)quad).getPositions()[2].position);
                Vec3d vec3d3 = vec3d2.crossProduct(vec3d).normalize();
                float f = (float)vec3d3.x;
                float g = (float)vec3d3.y;
                float h = (float)vec3d3.z;
                if (((TexturedQuadAccessor)quad).getField_1507()) 
                {
                    f = -f;
                    g = -g;
                    h = -h;
                }
                for (int i3 = 0; i3 < 4; ++i3) 
                {
                    TexturePosition texturePosition = ((TexturedQuadAccessor)quad).getPositions()[i3];
                    bufferBuilder.vertex(texturePosition.position.x * (double)scale, texturePosition.position.y * (double)scale, texturePosition.position.z * (double)scale).texture(texturePosition.u, texturePosition.v).normal(f, g, h).next();
                }
            }
        }
        
        bufferBuilder.end();
        ghostBufferBuilder = new GhostBufferBuilder(bufferBuilder);
        bufferBuilder.reset();*/
        
        //GL11.glEndList();
        customBufferBuilder.begin(7, VertexFormats.ENTITY);
        for (int i = 0; i < this.cuboids.size(); ++i) 
        {
            //this.cuboids.get(i).draw(customBufferBuilder, scale);
            ModelBox currentCuboid = cuboids.get(i);

            for (int i2 = 0; i2 < ((ModelBoxAccessor)currentCuboid).getQuads().length; ++i2) 
            {
                TexturedQuad currentQuad = ((ModelBoxAccessor)currentCuboid).getQuads()[i2];

                Vec3d vec3d = ((TexturedQuadAccessor)currentQuad).getPositions()[1].position.reverseSubtract(((TexturedQuadAccessor)currentQuad).getPositions()[0].position);
                Vec3d vec3d2 = ((TexturedQuadAccessor)currentQuad).getPositions()[1].position.reverseSubtract(((TexturedQuadAccessor)currentQuad).getPositions()[2].position);
                Vec3d vec3d3 = vec3d2.crossProduct(vec3d).normalize();
                float f = (float)vec3d3.x;
                float g = (float)vec3d3.y;
                float h = (float)vec3d3.z;
                if (((TexturedQuadAccessor)currentQuad).getField_1507()) 
                {
                    f = -f;
                    g = -g;
                    h = -h;
                }
                for (int i3 = 0; i3 < 4; ++i3) 
                {
                    TexturePosition texturePosition = ((TexturedQuadAccessor)currentQuad).getPositions()[i3];
                    customBufferBuilder.vertex(texturePosition.position.x * (double)scale, texturePosition.position.y * (double)scale, texturePosition.position.z * (double)scale).texture(texturePosition.u, texturePosition.v).normal(f, g, h).next();
                }
            }
        }

        customBufferBuilder.end();
        ghostBufferBuilder = new GhostBufferBuilder(customBufferBuilder);
        customBufferBuilder.reset();

        /*BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        for (int i = 0; i < this.cuboids.size(); ++i) {
            this.cuboids.get(i).draw(bufferBuilder, scale);
        }*/

        this.field_1611 = true;
    }

    private void CallListField1612()
    {
        CustomBufferRenderer.DrawNoReset(ghostBufferBuilder);
    }
}