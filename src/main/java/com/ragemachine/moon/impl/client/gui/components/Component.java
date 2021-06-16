

package com.ragemachine.moon.impl.client.gui.components;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.Hack;
import com.ragemachine.moon.impl.client.gui.MoonHackGui;
import com.ragemachine.moon.impl.client.gui.components.items.Item;
import com.ragemachine.moon.impl.client.gui.components.items.buttons.Button;
import com.ragemachine.moon.impl.client.modules.client.ClickGui;
import com.ragemachine.moon.impl.client.modules.client.Colors;
import com.ragemachine.moon.impl.util.ColorUtil;
import com.ragemachine.moon.impl.util.MathUtil;
import com.ragemachine.moon.impl.util.RenderUtil;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Component
        extends Hack {
    private ResourceLocation gear = new ResourceLocation("textures/gear.png");
    private ResourceLocation misc = new ResourceLocation("textures/misc.png");
    private ResourceLocation world = new ResourceLocation("textures/world.png");
    private ResourceLocation combat = new ResourceLocation("textures/combat.png");
    private ResourceLocation chat = new ResourceLocation("textures/chat.png");
    private ResourceLocation movement = new ResourceLocation("textures/movement.png");
    private ResourceLocation render = new ResourceLocation("textures/render.png");
    private ResourceLocation moonhack = new ResourceLocation("textures/moonhackimg.png");
    private int x;
    private int y;
    private int x2;
    private int y2;
    private float width;
    private float height;
    private boolean open;
    public boolean drag;
    private final ArrayList<Item> items = new ArrayList();
    private boolean hidden = false;
    private int w;

    public Component(String name, int x, int y,     boolean open) {
        super(name);
        this.x = x;
        this.y = y;
        this.width = 110;
        this.height = 18;
        this.open = open;
        this.setupItems();
    }

    public void setupItems() {
    }

    private void drag(int mouseX, int mouseY) {
        if (!this.drag) {
            return;
        }
        this.x = this.x2 + mouseX;
        this.y = this.y2 + mouseY;
    }


    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glPushMatrix();
        this.drag(mouseX, mouseY);
        rendertexturedFrame(100, misc, false,  new Color(255, 255, 255, 255).getRGB());
        float totalItemHeight = this.open ? this.getTotalItemHeight() - 2.0f : 0.0f;
        int color = new Color(255, 0, 0, 162).getRGB();
        int colorButton1 = new Color(67, 67, 67, 110).getRGB();
        int colorButton2 = new Color(22, 22, 22, 143).getRGB();
        int colorOutline1 = new Color(107, 107, 107, 110).getRGB();
        int colorOutline2 = new Color(62, 62, 62, 143).getRGB();
        if (Colors.getInstance().isEnabled()) {
                int n = color = ClickGui.getInstance().colorSync.getValue() ? Colors.getInstance().getCurrentColorHex() : ColorUtil.toARGB(ClickGui.getInstance().topRed.getValue(), ClickGui.getInstance().topGreen.getValue(), ClickGui.getInstance().topBlue.getValue(), ClickGui.getInstance().topAlpha.getValue());
            }
        if (ClickGui.getInstance().rainbowRolling.getValue() && ClickGui.getInstance().colorSync.getValue() && Colors.getInstance().rainbow.getValue()) {
            RenderUtil.drawGradientRect((float)this.x, (float)this.y - 1.5f, (float)this.width, (float)(this.height - 4), MathUtil.clamp(this.y, 0, this.renderer.scaledHeight), (int) MathUtil.clamp(this.y + this.height - 4, 0, this.renderer.scaledHeight));
        } else {
            RenderUtil.drawGradientSideways(this.x, (float)this.y - 1.5f, this.x + this.width, this.y + this.height - 6, new Color(0,0,0,255).getRGB(), color);
        }
        if (this.open) {
            RenderUtil.drawGradientSideways(this.x, (float) this.y + 12.5f, this.x + this.width, (float) (this.y + this.height) + totalItemHeight, colorButton2 ,colorButton1);
            RenderUtil.drawBorderedRect(this.x + 0.4, (float) this.y + 12.5f + 0.1 - 13, this.x + this.width + 0.1, (float) (this.y + this.height) + totalItemHeight + 0.1, ClickGui.getGui().getOutlineWidth() - ClickGui.getGui().getOutlineWidth()*2, new Color(0, 0, 0, 0).getRGB(), color);
        }
        if (!this.open)  {
            RenderUtil.drawBorderedRect(this.x, (float)this.y - 1.5f, this.x + this.width, this.y + this.height - 6, ClickGui.getGui().getOutlineWidth() - ClickGui.getGui().getOutlineWidth()*2, new Color(0,0,0,0).getRGB(), color);
        }
        MoonHack.TEXT_MANAGER.drawStringWithShadow(this.getName(), (float)this.x + 3.0f, (float)this.y - 4.0f - (float)MoonHackGui.getClickGui().getTextOffset(), -1);
       /* // MISC
        Wrapper.getMinecraft().renderEngine.bindTexture(misc);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GlStateManager.enableTexture2D();
        RenderUtil.drawTexture( (float)this.x + 3.0f, (float)this.y - 4.0f - -5.9f, 20, 20, 0, 0, 1, 1);
        // WORLD
        Wrapper.getMinecraft().renderEngine.bindTexture(world);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GlStateManager.enableTexture2D();
        RenderUtil.drawTexture((float)this.x + 3.0f, (float)this.y - 4.0f - -5.9f, 20, 20, 0, 0, 1, 1);
        // COMBAT
        Wrapper.getMinecraft().renderEngine.bindTexture(combat);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GlStateManager.enableTexture2D();
        RenderUtil.drawTexture( (float)this.x + 3.0f, (float)this.y - 4.0f - -5.9f, 20, 20, 0, 0, 1, 1);
        // RENDER
        Wrapper.getMinecraft().renderEngine.bindTexture(render);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GlStateManager.enableTexture2D();
        RenderUtil.drawTexture((float)this.x + 3.0f, (float)this.y - 4.0f - -5.9f, 20, 20, 0, 0, 1, 1);
        // MOVEMENT
        Wrapper.getMinecraft().renderEngine.bindTexture(movement);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GlStateManager.enableTexture2D();
        RenderUtil.drawTexture((float)this.x + 3.0f, (float)this.y - 4.0f - -5.9f, 20, 20, 0, 0, 1, 1);
        // CLIENT
        Wrapper.getMinecraft().renderEngine.bindTexture(moonhack);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GlStateManager.enableTexture2D();
        RenderUtil.drawTexture((float)this.x + 3.0f, (float)this.y - 4.0f - -5.9f, 20, 20, 0, 0, 1, 1);
        //CHAT
        Wrapper.getMinecraft().renderEngine.bindTexture(chat);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GlStateManager.enableTexture2D();
        RenderUtil.drawTexture((float)this.x + 3.0f, (float)this.y - 4.0f - -5.9f, 20, 20, 0, 0, 1, 1);*/
        if (!this.open) return;
        float y = (float)(this.getY() + this.getHeight()) - 3.0f;
        Iterator<Item> iterator = this.getItems().iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item.isHidden()) continue;
            item.setLocation((float)this.x + 2.0f, y);
            item.setWidth(this.getWidth() - 4);
            item.drawScreen(mouseX, mouseY, partialTicks);
            y += (float)item.getHeight() + 1.5f;
        }
        GL11.glPopMatrix();
    }

    public void rendertexturedFrame(int scale, ResourceLocation image, boolean originalColor, int color) {
        try{
            mc.getTextureManager().bindTexture(image);
            Gui.drawScaledCustomSizeModalRect((int)this.x + 3, (int)this.y - 4 - -5, 0, 0, 1, 1, 20, 20, 20, 20);
        }catch (Exception e){
            System.out.println("Error, couldn't load the texture");
            e.printStackTrace();
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.x2 = this.x - mouseX;
            this.y2 = this.y - mouseY;
            MoonHackGui.getClickGui().getComponents().forEach(component -> {
                if (!component.drag) return;
                component.drag = false;
            });
            this.drag = true;
            return;
        }
        if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            this.open = !this.open;
            mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord((SoundEvent)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
            return;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        if (releaseButton == 0) {
            this.drag = false;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public void onKeyTyped(char typedChar, int keyCode) {
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.onKeyTyped(typedChar, keyCode));
    }

    public void addButton(Button button) {
        this.items.add(button);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public boolean isOpen() {
        return this.open;
    }

    public final ArrayList<Item> getItems() {
        return this.items;
    }

    private boolean isHovering(int mouseX, int mouseY) {
        if (mouseX < this.getX()) return false;
        if (mouseX > this.getX() + this.getWidth()) return false;
        if (mouseY < this.getY()) return false;
        if (mouseY > this.getY() + this.getHeight() - (this.open ? 2 : 0)) return false;
        return true;
    }

    private float getTotalItemHeight() {
        float height = 0.0f;
        Iterator<Item> iterator = this.getItems().iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            height += (float)item.getHeight() + 1.5f;
        }
        return height;
    }
}

