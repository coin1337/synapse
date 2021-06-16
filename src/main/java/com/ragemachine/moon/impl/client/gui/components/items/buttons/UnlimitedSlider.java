

package com.ragemachine.moon.impl.client.gui.components.items.buttons;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.gui.MoonHackGui;
import com.ragemachine.moon.impl.client.modules.client.ClickGui;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.ColorUtil;
import com.ragemachine.moon.impl.util.MathUtil;
import com.ragemachine.moon.impl.util.RenderUtil;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class UnlimitedSlider extends Button
{
    public Value value;

    public UnlimitedSlider(final Value value) {
        super(value.getName());
        this.value = value;
        this.width = 15;
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (ClickGui.getInstance().rainbowRolling.getValue()) {
            final int color = ColorUtil.changeAlpha(colorMap.get(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)), MoonHack.getModuleManager.getModule(ClickGui.class).hoverAlpha.getValue());
            final int color2 = ColorUtil.changeAlpha(colorMap.get(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight)), MoonHack.getModuleManager.getModule(ClickGui.class).hoverAlpha.getValue());
            RenderUtil.drawGradientRect((float)(int)this.x, (float)(int)this.y, this.width + 7.4f, (float)this.height, color, color2);
        }
        else {
            RenderUtil.drawRect(this.x, this.y, this.x + this.width + 7.4f, this.y + this.height - 0.5f, this.isHovering(mouseX, mouseY) ? MoonHack.COLOR_MANAGER.getColorWithAlpha(MoonHack.getModuleManager.getModule(ClickGui.class).alpha.getValue()) : MoonHack.COLOR_MANAGER.getColorWithAlpha(MoonHack.getModuleManager.getModule(ClickGui.class).hoverAlpha.getValue()));
        }
        MoonHack.TEXT_MANAGER.drawStringWithShadow(" - " + this.value.getName() + " " + "" + this.value.getValue() + "" + " +", this.x + 2.3f, this.y - 1.7f - MoonHackGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            UnlimitedSlider.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            if (this.isRight(mouseX)) {
                if (this.value.getValue() instanceof Double) {
                    this.value.setValue((double)this.value.getValue() + 1.0);
                }
                else if (this.value.getValue() instanceof Float) {
                    this.value.setValue((float)this.value.getValue() + 1.0f);
                }
                else if (this.value.getValue() instanceof Integer) {
                    this.value.setValue((int)this.value.getValue() + 1);
                }
            }
            else if (this.value.getValue() instanceof Double) {
                this.value.setValue((double)this.value.getValue() - 1.0);
            }
            else if (this.value.getValue() instanceof Float) {
                this.value.setValue((float)this.value.getValue() - 1.0f);
            }
            else if (this.value.getValue() instanceof Integer) {
                this.value.setValue((int)this.value.getValue() - 1);
            }
        }
    }

    @Override
    public void update() {
        this.setHidden(!this.value.isVisible());
    }

    @Override
    public float getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
    }

    @Override
    public boolean getState() {
        return true;
    }

    public boolean isRight(final int x) {
        return x > this.x + (this.width + 7.4f) / 2.0f;
    }
}
