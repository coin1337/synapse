

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
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;

public class StringButton extends Button
{
    private Value value;
    public boolean isListening;
    private CurrentString currentString;

    public StringButton(final Value value) {
        super(value.getName());
        this.currentString = new CurrentString("");
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
        if (this.isListening) {
            MoonHack.TEXT_MANAGER.drawStringWithShadow(this.currentString.getString() + MoonHack.TEXT_MANAGER.getIdleSign(), this.x + 2.3f, this.y - 1.7f - MoonHackGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
        else {
            MoonHack.TEXT_MANAGER.drawStringWithShadow((this.value.shouldRenderName() ? (this.value.getName() + " ") : "") + this.value.getValue(), this.x + 2.3f, this.y - 1.7f - MoonHackGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            StringButton.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }

    @Override
    public void onKeyTyped(final char typedChar, final int keyCode) {
        if (this.isListening) {
            if (keyCode == 1) {
                return;
            }
            if (keyCode == 28) {
                this.enterString();
            }
            else if (keyCode == 14) {
                this.setString(removeLastChar(this.currentString.getString()));
            }
            else {
                Label_0122: {
                    if (keyCode == 47) {
                        if (!Keyboard.isKeyDown(157)) {
                            if (!Keyboard.isKeyDown(29)) {
                                break Label_0122;
                            }
                        }
                        try {
                            this.setString(this.currentString.getString() + Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
                if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                    this.setString(this.currentString.getString() + typedChar);
                }
            }
        }
    }

    @Override
    public void update() {
        this.setHidden(!this.value.isVisible());
    }

    private void enterString() {
        if (this.currentString.getString().isEmpty()) {
            this.value.setValue(this.value.getDefaultValue());
        }
        else {
            this.value.setValue(this.currentString.getString());
        }
        this.setString("");
        super.onMouseClick();
    }

    @Override
    public float getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
        this.isListening = !this.isListening;
    }

    @Override
    public boolean getState() {
        return !this.isListening;
    }

    public void setString(final String newString) {
        this.currentString = new CurrentString(newString);
    }

    public static String removeLastChar(final String str) {
        String output = "";
        if (str != null && str.length() > 0) {
            output = str.substring(0, str.length() - 1);
        }
        return output;
    }

    public static class CurrentString
    {
        private String string;

        public CurrentString(final String string) {
            this.string = string;
        }

        public String getString() {
            return this.string;
        }
    }
}
