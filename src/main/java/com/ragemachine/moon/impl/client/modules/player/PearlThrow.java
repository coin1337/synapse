package com.ragemachine.moon.impl.client.modules.player;

import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Bind;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class PearlThrow extends Module {

    private Value<Mode> mode = addSetting(new Value("Mode", Mode.MIDDLECLICK));
     private Value<Bind> pearlBind = addSetting(new Value("Pearl Bind", new Bind(-1)));


    private boolean clicked = false;
    private boolean accessedViaBind = false;

    public PearlThrow() {
        super("PearlThrow", "Throws a pearl", Category.PLAYER, false, false, false);
    }

    @Override
    public void onEnable() {
        if(!fullNullCheck() && mode.getValue() == Mode.TOGGLE) {
            throwPearl();
            this.disable();
        }
    }

    @Override
    public void onTick() {
        if(mode.getValue() == Mode.MIDDLECLICK) {
            if(Mouse.isButtonDown(2)) {
                if(!clicked) {
                    throwPearl();
                }
                clicked = true;
            } else {
                clicked = false;
            }
        }
        if (pearlBind.getValue().isDown()) {
            if(!clicked) {
                throwPearl();
            }
            clicked = true;
        } else {
            clicked = false;
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(Keyboard.getEventKeyState()) {
            if (pearlBind.getValue().getKey() == Keyboard.getEventKey()) {
                accessedViaBind = true;
                throwPearl();
                this.toggle();
            }
        }
    }

    private void throwPearl() {
        int pearlSlot = InventoryUtil.findHotbarBlock(ItemEnderPearl.class);
        boolean offhand = mc.player.getHeldItemOffhand().getItem() == Items.ENDER_PEARL;
        if(pearlSlot != -1 || offhand) {
            int oldslot = mc.player.inventory.currentItem;
            mc.playerController.processRightClick(mc.player, mc.world, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            if(!offhand) {
                InventoryUtil.switchToHotbarSlot(pearlSlot, false);
                InventoryUtil.switchToHotbarSlot(oldslot, false);
            }
        }
    }

    public enum Mode {
        TOGGLE,
        MIDDLECLICK
    }


}
