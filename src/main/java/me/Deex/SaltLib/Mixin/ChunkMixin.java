package me.Deex.SaltLib.Mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;

@Mixin(Chunk.class)
public class ChunkMixin 
{
    private final float _1over16 = 1f / 16f;

    @Shadow
    private int field_4743; //Might not work
    
    private int jInMethod_3923 = 0;
    private float kInMethod_3923 = 0;

    @Shadow
    @Final
    private ChunkSection[] chunkSections; //Might not work

    @Shadow
    @Final
    private World world;

    @Shadow
    @Final
    public int chunkX;

    @Shadow
    @Final
    public int chunkZ;

    //Currently slower thsn original implementation

    /*@Overwrite
    public void method_3923()
    {
        BlockPos blockPos = new BlockPos(this.chunkX << 4, 0, this.chunkZ << 4);
        for (int i = 0; i < 8; ++i) 
        {
            if (this.field_4743 >= 4096) 
            {
                return;
            }
            
            if (jInMethod_3923 >= 16)
            {
                jInMethod_3923 -= 16;
            }

            if (kInMethod_3923 >= 16)
            {
                kInMethod_3923 -= 16;
            }

            int k = (int)kInMethod_3923;
            int l = this.field_4743 / 256;
            ++this.field_4743;
            for (int m = 0; m < 16; ++m) 
            {
                boolean bl;
                BlockPos blockPos2 = blockPos.add(k, (jInMethod_3923 << 4) + m, l);
                bl = m == 0 || m == 15 || k == 0 || k == 15 || l == 0 || l == 15;
                if ((this.chunkSections[jInMethod_3923] != null || !bl) && (this.chunkSections[jInMethod_3923] == null || this.chunkSections[jInMethod_3923].getBlockAtPos(k, m, l).getMaterial() != Material.AIR)) continue;
                for (Direction direction : Direction.values()) 
                {
                    BlockPos blockPos3 = blockPos2.offset(direction);
                    if (this.world.getBlockState(blockPos3).getBlock().getLightLevel() <= 0) continue;
                    this.world.method_8568(blockPos3);
                }
                this.world.method_8568(blockPos2);
            }

            ++jInMethod_3923;
            kInMethod_3923 += _1over16;
        }
    }*/
}