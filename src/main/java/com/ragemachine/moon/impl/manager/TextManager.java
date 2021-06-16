package com.ragemachine.moon.impl.manager;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.Hack;
import com.ragemachine.moon.impl.client.gui.font.CustomFont;
import com.ragemachine.moon.impl.client.modules.client.ClickGui;
import com.ragemachine.moon.impl.util.Timer;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class TextManager extends Hack {

    private CustomFont customFont = new CustomFont(new Font("Nirmala UI Semilight", 0, 17), true, false);

    public int scaledWidth;
    public int scaledHeight;
    public int scaleFactor;
    public boolean font;

    private final Timer idleTimer = new Timer();
    private boolean idling;

    public TextManager() {
        this.updateResolution();
    }

    public void init(boolean startup) {
        ClickGui clickGui = MoonHack.getModuleManager.getModule(ClickGui.class);
        try {
            this.setFontRenderer(new Font("Nirmala UI Semilight", 0, clickGui.textSize.getValue()), true, true);
        } catch(Exception ignored) {}
    }

    public void drawStringWithShadow(String text, float x, float y, int color) {
        this.drawString(text, x, y, color, true);
    }

    public void drawString(String text, float x, float y, int color, boolean shadow) {
        if(!MoonHack.getModuleManager.getModule(ClickGui.class).isEnabled()) {
            if(shadow) {
                this.customFont.drawStringWithShadow(text, x, y, color);
            } else {
                this.customFont.drawString(text, x, y, color);
            }
            return;
        }
        if(MoonHack.getModuleManager.getModule(ClickGui.class).isEnabled()) {
            if(shadow) {
                this.customFont.drawStringWithShadow(text, x, y, color);
            } else {
                this.customFont.drawString(text, x, y, color);
            }
            return;
        }

        mc.fontRenderer.drawString(text, x, y, color, shadow);
    }

    public int getStringWidth(String text) {
        if(!MoonHack.getModuleManager.isModuleEnabled(ClickGui.class)) {
            return this.customFont.getStringWidth(text);
        }
        if(MoonHack.getModuleManager.isModuleEnabled(ClickGui.class)) {
            return this.customFont.getStringWidth(text);
        }
        return mc.fontRenderer.getStringWidth(text);
    }

    public int getFontHeight() {
        if(!MoonHack.getModuleManager.isModuleEnabled(ClickGui.class)) {
            String text = "A";
            return (int) this.customFont.getStringHeight(text);
        }
        if(MoonHack.getModuleManager.isModuleEnabled(ClickGui.class)) {
            String text = "A";
            return (int) this.customFont.getStringHeight(text);
        }
        return mc.fontRenderer.FONT_HEIGHT;
    }

    public void setFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        this.customFont = new CustomFont(font, antiAlias, fractionalMetrics);
    }

    public Font getCurrentFont() {
        return this.customFont.getFont();
    }

    public void updateResolution() {
        this.scaledWidth = mc.displayWidth;
        this.scaledHeight = mc.displayHeight;
        this.scaleFactor = 1;
        boolean flag = mc.isUnicode();
        int i = mc.gameSettings.guiScale;

        if (i == 0) {
            i = 1000;
        }

        while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }

        if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }

        double scaledWidthD = (double) this.scaledWidth / (double) this.scaleFactor;
        double scaledHeightD = (double) this.scaledHeight / (double) this.scaleFactor;
        this.scaledWidth = MathHelper.ceil(scaledWidthD);
        this.scaledHeight = MathHelper.ceil(scaledHeightD);
    }

    public String getIdleSign() {
        if(idleTimer.passedMs(500)) {
            idling = !idling;
            idleTimer.reset();
        }

        if(idling) {
            return "_";
        }
        return "";
    }
}
