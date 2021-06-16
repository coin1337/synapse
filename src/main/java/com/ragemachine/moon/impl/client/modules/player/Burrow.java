package com.ragemachine.moon.impl.client.modules.player;

import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.IMinecraft;
import com.ragemachine.moon.impl.util.InteractUtil;
import com.ragemachine.moon.impl.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Iterator;

public class Burrow extends Module {
    private Value<Boolean> rotate;
    private Value<Integer> delay;
    int lastHotbarSlot;
    int playerHotbarSlot;
    boolean isSneaking;
    Timer timer;

/*
* Thanks MoneyMod
* Also this module is trash so imma rewrite it l8r
 */
    public Burrow() {
        super("Burrow", "Places a block and burrows you into it", Category.PLAYER,true, false, true);
        rotate = (Value<Boolean>) addSetting(new Value("Rotate", true));
        delay = (Value<Integer>) addSetting(new Value("Delay", 3, 0, 20));
        this.timer = new  Timer();
    }
}

