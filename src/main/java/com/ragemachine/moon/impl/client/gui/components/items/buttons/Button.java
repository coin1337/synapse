package com.ragemachine.moon.impl.client.gui.components.items.buttons;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.gui.MoonHackGui;
import com.ragemachine.moon.impl.client.gui.components.Component;
import com.ragemachine.moon.impl.client.gui.components.items.Item;
import com.ragemachine.moon.impl.client.modules.client.ClickGui;
import com.ragemachine.moon.impl.util.ColorUtil;
import com.ragemachine.moon.impl.util.MathUtil;
import com.ragemachine.moon.impl.util.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class Button extends Item {
    private ResourceLocation logo = new ResourceLocation("textures/geaer.png");
    public Map<Integer, Integer> colorMap;

    private boolean state;

    public Button(String name) {
        super(name);
        this.height = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (ClickGui.getInstance().rainbowRolling.getValue()) {
            final int color = ColorUtil.changeAlpha(colorMap.get(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)), MoonHack.getModuleManager.getModule(ClickGui.class).hoverAlpha.getValue());
            final int color2 = ColorUtil.changeAlpha(colorMap.get(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight)), MoonHack.getModuleManager.getModule(ClickGui.class).hoverAlpha.getValue());
            RenderUtil.drawGradientRect(this.x, this.y, (float)this.width, this.height - 0.5f, this.getState() ? (this.isHovering(mouseX, mouseY) ? color : colorMap.get(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight))) : (this.isHovering(mouseX, mouseY) ? -2007673515 : 290805077), this.getState() ? (this.isHovering(mouseX, mouseY) ? color2 : colorMap.get(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight))) : (this.isHovering(mouseX, mouseY) ? -2007673515 : 290805077));
        }
        else {
            RenderUtil.drawRect(this.x, this.y, this.x + this.width, this.y + this.height - 0.5f, this.getState() ? (this.isHovering(mouseX, mouseY) ? MoonHack.COLOR_MANAGER.getColorWithAlpha(MoonHack.getModuleManager.getModule(ClickGui.class).alpha.getValue()) : MoonHack.COLOR_MANAGER.getColorWithAlpha(MoonHack.getModuleManager.getModule(ClickGui.class).hoverAlpha.getValue())) : (this.isHovering(mouseX, mouseY) ? -2007673515 : 290805077));
        }
        MoonHack.TEXT_MANAGER.drawStringWithShadow(this.getName(), this.x + 2.3f, this.y - 2.0f - MoonHackGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        //RenderUtil.drawBorderedRect(x + 2, y + 2, x+width + 2, y + height - 0.5f + 2, 0.8, new Color(0, 0, 0, 0).getRGB(), new Color(62, 62, 62, 220).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isHovering(mouseX, mouseY)) {
            onMouseClick();
        }
    }

    public void onMouseClick() {
        state = !state;
        toggle();
        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public void toggle() {}

    public boolean getState() {
        return state;
    }

    @Override
    public float getHeight() {
        return 14;
    }

    public boolean isHovering(int mouseX, int mouseY) {
        for (Component component : MoonHackGui.getClickGui().getComponents()) {
            if (component.drag) {
                return false;
            }
        }
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + height;
    }
}
