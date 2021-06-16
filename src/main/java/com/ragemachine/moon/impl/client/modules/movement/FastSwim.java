package com.ragemachine.moon.impl.client.modules.movement;

import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;

public class FastSwim extends Module {

    // $FF: synthetic field
    int divider = 5;

    public Value<Boolean> up;
    public Value<Boolean> down;
    public Value<Boolean> forward;
    public Value<Boolean> sprint;
    public Value<Boolean> only2b;

    public FastSwim() {
        super("FastSwim", "Cool Swim", Category.MOVEMENT, true, false, true);
        up = (Value<Boolean>) addSetting(new Value("Upwards", true));
        down = (Value<Boolean>) addSetting(new Value("Downwards", true));
        forward = (Value<Boolean>) addSetting(new Value("Forward", true));
        sprint = (Value<Boolean>) addSetting(new Value("AutSprint", true));
        only2b = (Value<Boolean>) addSetting(new Value("2b2t", true));
    }

    public void onUpdate() {
        int var1;
        if ((Boolean)this.only2b.getValue() && !mc.isSingleplayer() && mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.equalsIgnoreCase("2b2t.org")) {
            if ((Boolean)this.sprint.getValue() && (mc.player.isInLava() || mc.player.isInWater())) {
                mc.player.setSprinting(true);
            }

            if ((mc.player.isInWater() || mc.player.isInLava()) && mc.gameSettings.keyBindJump.isKeyDown() && (Boolean)this.up.getValue()) {
                mc.player.motionY = 0.725D / (double)this.divider;
            }

            if (mc.player.isInWater() || mc.player.isInLava()) {
                if ((!(Boolean)this.forward.getValue() || !mc.gameSettings.keyBindForward.isKeyDown()) && !mc.gameSettings.keyBindLeft.isKeyDown() && !mc.gameSettings.keyBindRight.isKeyDown() && !mc.gameSettings.keyBindBack.isKeyDown()) {
                    mc.player.jumpMovementFactor = 0.0F;
                } else {
                    mc.player.jumpMovementFactor = 0.34F / (float)this.divider;
                }
            }

            if (mc.player.isInWater() && (Boolean)this.down.getValue() && mc.gameSettings.keyBindSneak.isKeyDown()) {
                var1 = this.divider * -1;
                mc.player.motionY = 2.2D / (double)var1;
            }

            if (mc.player.isInLava() && (Boolean)this.down.getValue() && mc.gameSettings.keyBindSneak.isKeyDown()) {
                var1 = this.divider * -1;
                mc.player.motionY = 0.91D / (double)var1;
            }
        }

        if (!(Boolean)this.only2b.getValue()) {
            if ((Boolean)this.sprint.getValue() && (mc.player.isInLava() || mc.player.isInWater())) {
                mc.player.setSprinting(true);
            }

            if ((mc.player.isInWater() || mc.player.isInLava()) && mc.gameSettings.keyBindJump.isKeyDown() && (Boolean)this.up.getValue()) {
                mc.player.motionY = 0.725D / (double)this.divider;
            }

            if (mc.player.isInWater() || mc.player.isInLava()) {
                if ((!(Boolean)this.forward.getValue() || !mc.gameSettings.keyBindForward.isKeyDown()) && !mc.gameSettings.keyBindLeft.isKeyDown() && !mc.gameSettings.keyBindRight.isKeyDown() && !mc.gameSettings.keyBindBack.isKeyDown()) {
                    mc.player.jumpMovementFactor = 0.0F;
                } else {
                    mc.player.jumpMovementFactor = 0.34F / (float)this.divider;
                }
            }

            if (mc.player.isInWater() && (Boolean)this.down.getValue() && mc.gameSettings.keyBindSneak.isKeyDown()) {
                var1 = this.divider * -1;
                mc.player.motionY = 2.2D / (double)var1;
            }

            if (mc.player.isInLava() && (Boolean)this.down.getValue() && mc.gameSettings.keyBindSneak.isKeyDown()) {
                var1 = this.divider * -1;
                mc.player.motionY = 0.91D / (double)var1;
            }
        }

    }
}
