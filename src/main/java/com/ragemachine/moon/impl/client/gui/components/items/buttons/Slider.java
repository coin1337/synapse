package com.ragemachine.moon.impl.client.gui.components.items.buttons;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.gui.MoonHackGui;
import com.ragemachine.moon.impl.client.gui.components.Component;
import com.ragemachine.moon.impl.client.modules.client.ClickGui;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.RenderUtil;
import com.ragemachine.moon.impl.util.TextUtil;
import org.lwjgl.input.Mouse;

public class Slider extends Button {

    public Value value;
    private Number min;
    private Number max;
    private int difference;

    public Slider(Value value) {
        super(value.getName());
        this.value = value;
        this.min = (Number) value.getMin();
        this.max = (Number) value.getMax();
        this.difference = max.intValue() - min.intValue();
        width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        dragSetting(mouseX, mouseY);
        RenderUtil.drawRect(x, y, x + width + 7.4F, y + height - 0.5f, !isHovering(mouseX, mouseY) ? 0x11555555 : 0x88555555);
        RenderUtil.drawRect(x, y, ((Number) value.getValue()).floatValue() <= min.floatValue() ? x : x + (width + 7.4F) * partialMultiplier(), y + height - 0.5f, !isHovering(mouseX, mouseY) ? MoonHack.COLOR_MANAGER.getColorWithAlpha((MoonHack.getModuleManager.getModule(ClickGui.class)).hoverAlpha.getValue()) : MoonHack.COLOR_MANAGER.getColorWithAlpha(((MoonHack.getModuleManager.getModule(ClickGui.class)).alpha.getValue())));
        MoonHack.TEXT_MANAGER.drawStringWithShadow(getName() + " " + TextUtil.GRAY + (value.getValue() instanceof Float ? ((Number) value.getValue()) : ((Number) value.getValue()).doubleValue()), x + 2.3F, y - 1.7F - MoonHackGui.getClickGui().getTextOffset(), 0xFFFFFFFF);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isHovering(mouseX, mouseY)) {
            setSettingFromX(mouseX);
        }
    }

    @Override
    public boolean isHovering(int mouseX, int mouseY) {
        for (Component component : MoonHackGui.getClickGui().getComponents()) {
            if (component.drag) {
                return false;
            }
        }
        return mouseX >= getX() && mouseX <= getX() + getWidth() + 8 && mouseY >= getY() && mouseY <= getY() + height;
    }

    @Override
    public void update() {
        this.setHidden(!value.isVisible());
    }

    private void dragSetting(int mouseX, int mouseY) {
        if(isHovering(mouseX, mouseY) && Mouse.isButtonDown(0)) {
            setSettingFromX(mouseX);
        }
    }

    @Override
    public float getHeight() {
        return 14;
    }

    private void setSettingFromX(int mouseX) {
        float percent = (mouseX - x) / (width + 7.4F);
        if(value.getValue() instanceof Double) {
            double result = (Double) value.getMin() + (difference * percent);
            value.setValue(Math.round(10.0 * result) / 10.0);
        } else if (value.getValue() instanceof Float) {
            float result = (Float) value.getMin() + (difference * percent);
            value.setValue(Math.round(10.0f * result) / 10.0f);
        } else if (value.getValue() instanceof Integer) {
            value.setValue(((Integer) value.getMin() + (int)(difference * percent)));
        }
    }

    private float middle() {
        return max.floatValue() - min.floatValue();
    }

    private float part() {
        return ((Number) value.getValue()).floatValue() - min.floatValue();
    }

    private float partialMultiplier() {
        return part() / middle();
    }
}
