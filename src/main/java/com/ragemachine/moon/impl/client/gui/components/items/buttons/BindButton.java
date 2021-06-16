package com.ragemachine.moon.impl.client.gui.components.items.buttons;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.gui.MoonHackGui;
import com.ragemachine.moon.impl.client.modules.client.ClickGui;
import com.ragemachine.moon.impl.client.setting.Bind;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.RenderUtil;
import com.ragemachine.moon.impl.util.TextUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class BindButton extends Button {

    private Value value;
    public boolean isListening;

    public BindButton(Value value) {
        super(value.getName());
        this.value = value;
        width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(x, y, x + width + 7.4F, y + height - 0.5f, getState() ? (!isHovering(mouseX, mouseY) ? MoonHack.COLOR_MANAGER.getColorWithAlpha(((ClickGui)MoonHack.getModuleManager.getModuleByName("ClickGui")).hoverAlpha.getValue()) : MoonHack.COLOR_MANAGER.getColorWithAlpha(((ClickGui)MoonHack.getModuleManager.getModuleByName("ClickGui")).alpha.getValue())) : !isHovering(mouseX, mouseY) ? 0x11555555 : 0x88555555);
        if (isListening) {
            MoonHack.TEXT_MANAGER.drawStringWithShadow("Listening...", x + 2.3F, y - 1.7F - MoonHackGui.getClickGui().getTextOffset(), getState() ? 0xFFFFFFFF : 0xFFAAAAAA);
        } else {
            MoonHack.TEXT_MANAGER.drawStringWithShadow(value.getName() + " " + TextUtil.GRAY + ((Bind) value.getValue()).toString(), x + 2.3F, y - 1.7F - MoonHackGui.getClickGui().getTextOffset(), getState() ? 0xFFFFFFFF : 0xFFAAAAAA);
        }
    }

    @Override
    public void update() {
        this.setHidden(!value.isVisible());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isHovering(mouseX, mouseY)) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        if(isListening) {
            Bind bind = new Bind(keyCode);
            if(bind.toString().equalsIgnoreCase("Escape")) {
                return;
            } else if(bind.toString().equalsIgnoreCase("Delete")) {
                bind = new Bind(-1);
            }
            value.setValue(bind);
            super.onMouseClick();
        }
    }

    @Override
    public float getHeight() {
        return 14;
    }

    public void toggle() {
        isListening = !isListening;
    }

    public boolean getState() {
        return !isListening;
    }
}
