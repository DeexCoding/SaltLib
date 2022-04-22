package me.Deex.SaltLib.Mixin;

import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

@Mixin(World.class)
public class WorldMixin 
{
    private static final int MIN_POWER_LEVEL = 0;
    private static final int MAX_POWER_LEVEL = 15;
    private static final int MAX_POWER_FROM_OTHER_WIRE = 14;

    @Inject(method = "getReceivedRedstonePower", cancellable = true, at = @At(value = "HEAD"))
    private void getReceivedRedstonePower(BlockPos pos, CallbackInfoReturnable<Integer> returnValue) 
    {
        returnValue.setReturnValue(CalculatePower(pos));
    }

    private int CalculatePower(BlockPos pos)
    {
        World world = ((World)(Object)this);
        int power = MIN_POWER_LEVEL;

        for (Direction dir : Direction.DirectionType.VERTICAL)
        {
            BlockPos side = pos.offset(dir);
            BlockState neighbor = world.getBlockState(side);

            if (!(neighbor.getBlock() instanceof AirBlock) && !(neighbor.getBlock() instanceof RedstoneWireBlock))
            {
                power = Math.max(power, GetPowerFromVertical(world, side, neighbor, dir));

                if (power >= MAX_POWER_LEVEL)
                {
                    return MAX_POWER_LEVEL;
                }
            }
        }

        BlockPos up = pos.up();

        for (Direction dir : Direction.DirectionType.HORIZONTAL) {

            power = Math.max(power, GetPowerFromHorizontal(world, pos.offset(dir), dir, !world.getBlockAt(up).isNormalBlock())); //May need to use isFullCube()
            
            if (power >= MAX_POWER_LEVEL) 
            {
                return MAX_POWER_LEVEL;
            }
        }
        
        return power;
    }

    private int GetPowerFromVertical(World world, BlockPos pos, BlockState state, Direction dir)
    {
        int power = state.getBlock().getWeakRedstonePower(world, pos, state, dir);

        if (power >= MAX_POWER_LEVEL)
        {
            return MAX_POWER_LEVEL;
        }

        if (state.getBlock().isNormalBlock())
        {
            return Math.max(power, GetStrongPowerTo(world, pos, state, dir.getOpposite()));
        }

        return power;
    }

    private int GetPowerFromHorizontal(World world, BlockPos pos, Direction toDir, boolean checkWiresAbove) 
    {
        BlockState state = world.getBlockState(pos);
        
        if (state.getBlock() instanceof RedstoneWireBlock)
        {
            return ((RedstoneWireBlock)state.getBlock()).getData(state) - 1;
        }
        
        int power = state.getBlock().getWeakRedstonePower(world, pos, state, toDir);
        
        if (power >= MAX_POWER_LEVEL)
        {
            return MAX_POWER_LEVEL;
        }
        
        if (state.getBlock().isNormalBlock()) //May need to use isFullCube()
        {
            power = Math.max(power, GetStrongPowerTo(world, pos, state, toDir.getOpposite()));
            
            if (power >= MAX_POWER_LEVEL)
            {
                return MAX_POWER_LEVEL;
            }
            
            if (checkWiresAbove && power < MAX_POWER_FROM_OTHER_WIRE) 
            {
                BlockPos up = pos.up();
                BlockState aboveState = world.getBlockState(up);
                
                if (aboveState.getBlock() instanceof RedstoneWireBlock) 
                {
                    power = Math.max(power, ((RedstoneWireBlock)state.getBlock()).getData(state) - 1);
                }
            }
        } 
        else if (power < MAX_POWER_FROM_OTHER_WIRE) 
        {
            BlockPos down = pos.down();
            BlockState belowState = world.getBlockState(down);
            
            if (belowState.getBlock() instanceof RedstoneWireBlock) 
            {
                power = Math.max(power, ((RedstoneWireBlock)state.getBlock()).getData(state) - 1);
            }
        }
        
        return power;
    }

    private int GetStrongPowerTo(World world, BlockPos pos, BlockState state, Direction ignore)
    {
        int power = MIN_POWER_LEVEL;

        for (Direction dir : Direction.values())
        {
            if (dir != ignore)
            {
                BlockPos side = pos.offset(dir);
                BlockState neighbor = world.getBlockState(side);

                if  (!(neighbor.getBlock() instanceof AirBlock) && 
                !(neighbor.getBlock() instanceof RedstoneWireBlock))
                {
                    power = Math.max(power, state.getBlock().getStrongRedstonePower(world, side, neighbor, dir));

                    if (power >= MAX_POWER_LEVEL)
                    {
                        return MAX_POWER_LEVEL;
                    }
                }
            }
        }

        return power;
    }
}
