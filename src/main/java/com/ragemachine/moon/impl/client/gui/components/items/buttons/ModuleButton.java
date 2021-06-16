

package com.ragemachine.moon.impl.client.gui.components.items.buttons;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.gui.MoonHackGui;
import com.ragemachine.moon.impl.client.gui.components.items.Item;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.modules.client.ClickGui;
import com.ragemachine.moon.impl.client.setting.Bind;
import com.ragemachine.moon.impl.client.setting.Value;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

import java.util.ArrayList;
import java.util.List;

public class ModuleButton extends Button
{
    private final Module module;
    private List<Item> items;
    private boolean subOpen;

    public ModuleButton(final Module module) {
        super(module.getName());
        this.items = new ArrayList<Item>();
        this.module = module;
        this.initSettings();
    }

    public void initSettings() {
        final List<Item> newItems = new ArrayList<Item>();
        if (!this.module.getSettings().isEmpty()) {
            for (final Value value : this.module.getSettings()) {
                if (value.getValue() instanceof Boolean && !value.getName().equals("Enabled")) {
                    newItems.add(new BooleanButton(value));
                }
                if (value.getValue() instanceof Bind && !this.module.getName().equalsIgnoreCase("Hud")) {
                    newItems.add(new BindButton(value));
                }
                if (value.getValue() instanceof String || value.getValue() instanceof Character) {
                    newItems.add(new StringButton(value));
                }
                if (value.isNumberSetting()) {
                    if (value.hasRestriction()) {
                        newItems.add(new Slider(value));
                        continue;
                    }
                    newItems.add(new UnlimitedSlider(value));
                }
                if (value.isEnumSetting()) {
                    newItems.add(new EnumButton(value));
                }
            }
        }
        this.items = newItems;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (!this.items.isEmpty()) {
            MoonHack.TEXT_MANAGER.drawStringWithShadow((MoonHack.getModuleManager.getModule(ClickGui.class).getSettingByName("Buttons").getValueAsString()), x - 1.5F + width - 7.4F, y - 2F - MoonHackGui.getClickGui().getTextOffset(), 0xFFFFFFFF);
            if (subOpen) {
                float height = 1;
                for (Item item : this.items) {
                    if(!item.isHidden()) {
                        height += 15F;
                        item.setLocation(x + 1, y + height);
                        item.setHeight(15);
                        item.setWidth(width - 9);
                        item.drawScreen(mouseX, mouseY, partialTicks);
                    }
                    item.update();
                }
            }
        }
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!this.items.isEmpty()) {
            if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
                this.subOpen = !this.subOpen;
                ModuleButton.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }
            if (this.subOpen) {
                for (final Item item : this.items) {
                    if (!item.isHidden()) {
                        item.mouseClicked(mouseX, mouseY, mouseButton);
                    }
                }
            }
        }
    }

    @Override
    public void onKeyTyped(final char typedChar, final int keyCode) {
        super.onKeyTyped(typedChar, keyCode);
        if (!this.items.isEmpty() && this.subOpen) {
            for (final Item item : this.items) {
                if (!item.isHidden()) {
                    item.onKeyTyped(typedChar, keyCode);
                }
            }
        }
    }

    @Override
    public float getHeight() {
        if (this.subOpen) {
            int height = 14;
            for (final Item item : this.items) {
                if (!item.isHidden()) {
                    height += item.getHeight() + 1;
                }
            }
            return height + 2;
        }
        return 14;
    }

    public Module getModule() {
        return this.module;
    }

    @Override
    public void toggle() {
        this.module.toggle();
    }

    @Override
    public boolean getState() {
        return this.module.isEnabled();
    }
}
