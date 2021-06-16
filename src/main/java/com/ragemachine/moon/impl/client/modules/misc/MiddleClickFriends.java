package com.ragemachine.moon.impl.client.modules.misc;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.command.Command;
import com.ragemachine.moon.impl.client.gui.MoonHackGui;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Bind;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.TextUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class MiddleClickFriends extends Module {

    private final Value<Boolean> middleClick = addSetting(new Value("MiddleClick", true));
    private final Value<Boolean> keyboard = addSetting(new Value("Keyboard", false));
    private final Value<Bind> key = addSetting(new Value("KeyBind", new Bind(-1), v -> keyboard.getValue()));

    private boolean clicked = false;

    public MiddleClickFriends() {
        super("MiddleClickFriends", "Middleclick Friends.", Category.MISC, true, false, false);
    }

    @Override
    public void onUpdate() {
        if(Mouse.isButtonDown(2)) {
            if(!clicked && middleClick.getValue() && mc.currentScreen == null) {
                onClick();
            }
            clicked = true;
        } else {
            clicked = false;
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (keyboard.getValue() && Keyboard.getEventKeyState() && !(mc.currentScreen instanceof MoonHackGui) && key.getValue().getKey() == Keyboard.getEventKey()) {
            onClick();
        }
    }

    private void onClick() {
        RayTraceResult result = mc.objectMouseOver;
        if(result != null && result.typeOfHit == RayTraceResult.Type.ENTITY) {
            Entity entity = result.entityHit;
            if(entity instanceof EntityPlayer) {
                if(MoonHack.getFriendManager.isFriend(entity.getName())) {
                    MoonHack.getFriendManager.removeFriend(entity.getName());
                    Command.sendMessage(TextUtil.RED + entity.getName() + TextUtil.RESET + " unfriended.");
                } else {
                    MoonHack.getFriendManager.addFriend(entity.getName());
                    Command.sendMessage(TextUtil.AQUA + entity.getName() + TextUtil.RESET + " friended.");
                }
            }
        }
        clicked = true;
    }
}
