
package com.ragemachine.moon.impl.manager;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.Hack;
import com.ragemachine.moon.impl.client.modules.client.ClientControl;
import com.ragemachine.moon.impl.client.modules.movement.ReverseStep;
import com.ragemachine.moon.impl.client.modules.render.HoleESP;
import com.ragemachine.moon.impl.util.BlockUtil;
import com.ragemachine.moon.impl.util.EntityUtil;
import com.ragemachine.moon.impl.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class HoleManager
        extends Hack
        implements Runnable {
    private static final BlockPos[] surroundOffset = BlockUtil.toBlockPos(EntityUtil.getOffsets(0, true));
    private List<BlockPos> holes = new ArrayList<BlockPos>();
    private final List<BlockPos> midSafety = new ArrayList<BlockPos>();
    private final Timer syncTimer = new Timer();
    private ScheduledExecutorService executorService;
    private int lastUpdates = 0;
    private Thread thread;
    private final AtomicBoolean shouldInterrupt = new AtomicBoolean(false);
    private final Timer holeTimer = new Timer();

    public void update() {
        if (this.thread == null) {
            this.thread = new Thread(this);
        }
        if (this.thread != null && (this.thread.isInterrupted() || !this.thread.isAlive())) {
            this.thread = new Thread(this);
        }
        if (this.thread == null) return;
        if (this.thread.getState() != Thread.State.NEW) return;
        try {
            this.thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.syncTimer.reset();
        return;
    }

    public void settingChanged() {
        if (this.executorService != null) {
            this.executorService.shutdown();
        }
        if (this.thread == null) return;
        this.shouldInterrupt.set(true);
    }

    private ScheduledExecutorService getExecutor() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(this, 0L, 100, TimeUnit.MILLISECONDS);
        return service;
    }

    @Override
    public void run() {
        if (this.shouldInterrupt.get()) {
            this.shouldInterrupt.set(false);
            this.syncTimer.reset();
            Thread.currentThread().interrupt();
            return;
        }
        if (!HoleManager.fullNullCheck() && (HoleESP.getInstance().isOn())) {
            this.holes = this.calcHoles();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            this.thread.interrupt();
            e.printStackTrace();
        }
    };

    public List<BlockPos> getHoles() {
        return this.holes;
    }

    public List<BlockPos> getMidSafety() {
        return this.midSafety;
    }

    public List<BlockPos> getSortedHoles() {
        this.holes.sort(Comparator.comparingDouble(hole -> HoleManager.mc.player.getDistanceSq(hole)));
        return this.getHoles();
    }

    public List<BlockPos> calcHoles() {
        ArrayList<BlockPos> safeSpots = new ArrayList<BlockPos>();
        this.midSafety.clear();
        List<BlockPos> positions = BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)HoleManager.mc.player), HoleESP.getInstance().range.getValue().floatValue(), HoleESP.getInstance().range.getValue().intValue(), false, true, 0);
        Iterator<BlockPos> iterator = positions.iterator();
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            if (!HoleManager.mc.world.getBlockState(pos).getBlock().equals((Object)Blocks.AIR) || !HoleManager.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals((Object)Blocks.AIR) || !HoleManager.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals((Object)Blocks.AIR)) continue;
            boolean isSafe = true;
            boolean midSafe = true;
            for (BlockPos offset : surroundOffset) {
                Block block = HoleManager.mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock();
                if (BlockUtil.isBlockUnSolid(block)) {
                    midSafe = false;
                }
                if (block == Blocks.BEDROCK || block == Blocks.OBSIDIAN || block == Blocks.ENDER_CHEST || block == Blocks.ANVIL) continue;
                isSafe = false;
            }
            if (isSafe) {
                safeSpots.add(pos);
            }
            if (!midSafe) continue;
            this.midSafety.add(pos);
        }
        return safeSpots;
    }

    public boolean isSafe(BlockPos pos) {
        boolean isSafe = true;
        BlockPos[] arrblockPos = surroundOffset;
        int n = arrblockPos.length;
        int n2 = 0;
        while (n2 < n) {
            BlockPos offset = arrblockPos[n2];
            Block block = HoleManager.mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock();
            if (block != Blocks.BEDROCK) {
                return false;
            }
            ++n2;
        }
        return isSafe;
    }
}

