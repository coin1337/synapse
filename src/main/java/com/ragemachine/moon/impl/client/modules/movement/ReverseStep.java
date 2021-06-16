package com.ragemachine.moon.impl.client.modules.movement;

import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import net.minecraftforge.fml.common.Mod;

public class ReverseStep extends Module {
    public ReverseStep() {
        super("ReverseStep", "Step Down Blocks", Category.MOVEMENT, false, false, true);
    }

    public Value<Integer> height = this.addSetting(new Value("Height", 3, 1, 256));

    @Override
    public void onEnable() {
        if (mc.player.isInWater() || mc.player.isInLava() || mc.player.isOnLadder() || mc.gameSettings.keyBindJump.isKeyDown()) {
            return;
        }
        if (mc.player != null && mc.player.onGround && !mc.player.isInWater() && !mc.player.isOnLadder()) {
            for (double y = 0.0; y < this.height.getValue() + 0.5; y += 0.01) {
                if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
                    mc.player.motionY = -5.0;
                    break;
                }
            }
        }
    }

    @Mod.EventHandler
    public void onUpdate() {
        if (mc.player.isInWater() || mc.player.isInLava() || mc.player.isOnLadder() || mc.gameSettings.keyBindJump.isKeyDown()) {
            return;
        }
        if (mc.player != null && mc.player.onGround && !mc.player.isInWater() && !mc.player.isOnLadder()) {
            for (double y = 0.0; y < this.height.getValue() + 0.5; y += 0.01) {
                if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
                    mc.player.motionY = -10.0;
                    break;
                }
            }
        }
    }
}