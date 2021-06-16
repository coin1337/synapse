package com.ragemachine.moon.impl.client.modules.movement;

import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class Anchor extends Module {

    //removed since it was breaking movement for wtvr reason, ill look into it later
    public Anchor() {
        super("Anchor", "Stops your movement over a hole", Category.MOVEMENT, true, false, false);
        Anchor instance = this;
        this.holes = new ArrayList();
        this.Center = Vec3d.ZERO;
    }

    public Value<Integer> pitch = this.addSetting(new Value("Pitch", 10, 1, 256));
    private final ArrayList holes;
    int holeblocks;
    public static boolean AnchorING;
    private Vec3d Center;

/*
 public Vec3d GetCenter(double posX, double posY, double posZ) {
        double x = Math.floor(posX) + 0.5D;
        double y = Math.floor(posY);
        double z = Math.floor(posZ) + 0.5D;
        return new Vec3d(x, y, z);
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(WalkEvent event) {
        if (this.mc.player != null) {
            if (this.mc.player.rotationPitch >= (float)this.pitch.getValue()) {
                if (!this.isInHole(this.getPlayerPos().down(1)) && !this.isInHole(this.getPlayerPos().down(2)) && !this.isInHole(this.getPlayerPos().down(3)) && !this.isInHole(this.getPlayerPos().down(4))) {
                    AnchorING = false;
                } else {
                    AnchorING = true;
                        this.Center = this.GetCenter(this.mc.player.posX, this.mc.player.posY, this.mc.player.posZ);
                        double XDiff = Math.abs(this.Center.x - this.mc.player.posX);
                        double ZDiff = Math.abs(this.Center.z - this.mc.player.posZ);
                        if (XDiff <= 0.1D && ZDiff <= 0.1D) {
                            this.Center = Vec3d.ZERO;
                        } else {
                            double MotionX = this.Center.x - this.mc.player.posX;
                            double MotionZ = this.Center.z - this.mc.player.posZ;
                            this.mc.player.motionX = MotionX / 2.0D;
                            this.mc.player.motionZ = MotionZ / 2.0D;
                        }
                }
            }

        }
    }

    public boolean isInHole(BlockPos blockpos) {
        this.holeblocks = 0;
        if (this.mc.world.getBlockState(blockpos.add(0, 3, 0)).getBlock() == Blocks.AIR) {
            ++this.holeblocks;
        }

        if (this.mc.world.getBlockState(blockpos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
            ++this.holeblocks;
        }

        if (this.mc.world.getBlockState(blockpos.add(0, 1, 0)).getBlock() == Blocks.AIR) {
            ++this.holeblocks;
        }

        if (this.mc.world.getBlockState(blockpos.add(0, 0, 0)).getBlock() == Blocks.AIR) {
            ++this.holeblocks;
        }

        if (this.mc.world.getBlockState(blockpos.add(0, -1, 0)).getBlock() == Blocks.OBSIDIAN || this.mc.world.getBlockState(blockpos.add(0, -1, 0)).getBlock() == Blocks.BEDROCK) {
            ++this.holeblocks;
        }

        if (this.mc.world.getBlockState(blockpos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN || this.mc.world.getBlockState(blockpos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK) {
            ++this.holeblocks;
        }

        if (this.mc.world.getBlockState(blockpos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN || this.mc.world.getBlockState(blockpos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK) {
            ++this.holeblocks;
        }

        if (this.mc.world.getBlockState(blockpos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN || this.mc.world.getBlockState(blockpos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK) {
            ++this.holeblocks;
        }

        if (this.mc.world.getBlockState(blockpos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN || this.mc.world.getBlockState(blockpos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK) {
            ++this.holeblocks;
        }

        return this.holeblocks >= 9;
    }

    public void onDisable() {
        AnchorING = false;
        this.holeblocks = 0;
    }

    public BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(this.mc.player.posX), Math.floor(this.mc.player.posY), Math.floor(this.mc.player.posZ));
    }*/
}


