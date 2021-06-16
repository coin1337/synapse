

package com.ragemachine.moon.impl.client.gui.components.items.buttons;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.gui.MoonHackGui;
import com.ragemachine.moon.impl.client.modules.client.ClickGui;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.ColorUtil;
import com.ragemachine.moon.impl.util.MathUtil;
import com.ragemachine.moon.impl.util.RenderUtil;
import com.ragemachine.moon.impl.util.TextUtil;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class EnumButton extends Button
{
    public Value value;

    public EnumButton(final Value value) {
        super(value.getName());
        this.value = value;
        this.width = 15;
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (ClickGui.getInstance().rainbowRolling.getValue()) {
            final int color = ColorUtil.changeAlpha(colorMap.get(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)), MoonHack.getModuleManager.getModule(ClickGui.class).hoverAlpha.getValue());
            final int color2 = ColorUtil.changeAlpha(colorMap.get(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight)), MoonHack.getModuleManager.getModule(ClickGui.class).hoverAlpha.getValue());
            RenderUtil.drawGradientRect(this.x, this.y, this.width + 7.4f, this.height - 0.5f, this.getState() ? (this.isHovering(mouseX, mouseY) ? color : colorMap.get(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight))) : (this.isHovering(mouseX, mouseY) ? -2007673515 : 290805077), this.getState() ? (this.isHovering(mouseX, mouseY) ? color2 : colorMap.get(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight))) : (this.isHovering(mouseX, mouseY) ? -2007673515 : 290805077));
        }
        else {
            RenderUtil.drawRect(this.x, this.y, this.x + this.width + 7.4f, this.y + this.height - 0.5f, this.getState() ? (this.isHovering(mouseX, mouseY) ? MoonHack.COLOR_MANAGER.getColorWithAlpha(MoonHack.getModuleManager.getModule(ClickGui.class).alpha.getValue()) : MoonHack.COLOR_MANAGER.getColorWithAlpha(MoonHack.getModuleManager.getModule(ClickGui.class).hoverAlpha.getValue())) : (this.isHovering(mouseX, mouseY) ? -2007673515 : 290805077));
        }
        MoonHack.TEXT_MANAGER.drawStringWithShadow(value.getName() + " " + TextUtil.GRAY + value.currentEnumName(), x + 2.3F, y - 1.7F - MoonHackGui.getClickGui().getTextOffset(), getState() ? 0xFFFFFFFF : 0xFFAAAAAA);
    }

    @Override
    public void update() {
        this.setHidden(!this.value.isVisible());
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            EnumButton.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }

    @Override
    public float getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
        this.value.increaseEnum();
    }

    @Override
    public boolean getState() {
        return true;
    }
}
