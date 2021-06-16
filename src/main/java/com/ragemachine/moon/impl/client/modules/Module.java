
package com.ragemachine.moon.impl.client.modules;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.Hack;
import com.ragemachine.moon.api.event.events.ClientEvent;
import com.ragemachine.moon.api.event.events.Render2DEvent;
import com.ragemachine.moon.api.event.events.Render3DEvent;
import com.ragemachine.moon.api.event.events.RenderEvent;
import com.ragemachine.moon.impl.client.command.Command;
import com.ragemachine.moon.impl.client.setting.Bind;
import com.ragemachine.moon.impl.client.setting.Value;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

public class Module
        extends Hack {
    private final String description;
    private final Category category;
    public String tag;
    public Value<Boolean> enabled = this.addSetting(new Value<Boolean>("Enabled", false));
    public Value<Boolean> drawn = this.addSetting(new Value<Boolean>("Show", true));
    public Value<Bind> bind = this.addSetting(new Value<Bind>("Bind", new Bind(-1)));
    public Value<String> displayName;
    public boolean debug1;
    public boolean debug2;
    public boolean hidden;
    public float arrayListOffset = 0.0f;
    public float arrayListVOffset = 0.0f;
    public float offset;
    public float vOffset;
    public boolean sliding;

    public Module(String name, String description, Category category, boolean debug1, boolean hidden, boolean debug2) {
        super(name);
        this.displayName = this.addSetting(new Value<String>("DisplayName", name));
        this.description = description;
        this.category = category;
        this.debug1 = debug1;
        this.hidden = hidden;
        this.debug2 = debug2;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onToggle() {
    }

    public void onRender() {
    }

    public void onLoad() {
    }

    public void onTick() {
    }

    public void onLogin() {
    }

    public void onLogout() {
    }

    public void onUpdate() {
    }

    public void onRender2D(Render2DEvent event) {
    }

    public void onRender3D(Render3DEvent event) {
    }

    public void onUnload() {
    }

    public void onWorldRender(RenderEvent event) {
    }

    public String getDisplayInfo() {
        return null;
    }

    public boolean isOn() {
        return this.enabled.getValue();
    }

    public boolean isOff() {
        if (this.enabled.getValue() != false) return false;
        return true;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.enable();
            return;
        }
        this.disable();
    }

    public void enable() {
        this.enabled.setValue(true);
        this.onToggle();
        this.onEnable();
        if (!this.isOn()) return;
        if (!this.debug1) return;
        if (this.debug2) return;
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    public void disable() {
        if (this.debug1 && !this.debug2) {
            MinecraftForge.EVENT_BUS.unregister((Object)this);
        }
        this.enabled.setValue(false);
        this.onToggle();
        this.onDisable();
    }

    public void toggle() {
        ClientEvent event = new ClientEvent(!this.isEnabled() ? 1 : 0, this);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) return;
        this.setEnabled(!this.isEnabled());
    }

    public String getDisplayName() {
        return this.displayName.getValue();
    }

    public String getDescription() {
        return this.description;
    }

    public void setDisplayName(String name) {
        Module module = MoonHack.getModuleManager.getModuleByDisplayName(name);
        Module originalModule = MoonHack.getModuleManager.getModuleByName(name);
        if (module == null && originalModule == null) {
            Command.sendMessage(this.getDisplayName() + ", Original name: " + this.getName() + ", has been renamed to: " + name);
            this.displayName.setValue(name);
            return;
        }
        Command.sendMessage("\u00a7cA module of this name already exists.");
    }

    public boolean isSliding() {
        return this.sliding;
    }

    public boolean isDrawn() {
        return this.drawn.getValue();
    }

    public void setDrawn(boolean drawn) {
        this.drawn.setValue(drawn);
    }

    public Category getCategory() {
        return this.category;
    }

    public String getInfo() {
        return null;
    }

    public Bind getBind() {
        return this.bind.getValue();
    }

    public String getTag() {
        return this.tag;
    }

    public void setBind(int key) {
        this.bind.setValue(new Bind(key));
    }

    public boolean listening() {
        if (this.debug1) {
            if (this.isOn()) return true;
        }
        if (this.debug2) return true;
        return false;
    }

    public String getFullArrayString() {
        String string;
        if (this.getDisplayInfo() != null) {
            string = " [\u00a7r" + this.getDisplayInfo() + "\u00a78" + "]";
            return this.getDisplayName() + "\u00a78" + string;
        }
        string = "";
        return this.getDisplayName() + "\u00a78" + string;
    }


    public static enum Category {
        COMBAT("Combat"),
        MISC("Misc"),
        RENDER("Render"),
        MOVEMENT("Movement"),
        PLAYER("Player"),
        CHAT("Chat"),
        CLIENT("Synapse");

        private final String name;

        private Category(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

}

