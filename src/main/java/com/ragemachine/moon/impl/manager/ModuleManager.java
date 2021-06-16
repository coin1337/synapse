

package com.ragemachine.moon.impl.manager;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.api.event.events.Render2DEvent;
import com.ragemachine.moon.api.event.events.Render3DEvent;
import com.ragemachine.moon.impl.client.Hack;
import com.ragemachine.moon.impl.client.gui.MoonHackGui;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.modules.chat.AntiVanish;
import com.ragemachine.moon.impl.client.modules.client.*;
import com.ragemachine.moon.impl.client.modules.combat.*;
import com.ragemachine.moon.impl.client.modules.misc.*;
import com.ragemachine.moon.impl.client.modules.movement.*;
import com.ragemachine.moon.impl.client.modules.player.*;
import com.ragemachine.moon.impl.client.modules.render.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.input.Keyboard;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ModuleManager
        extends Hack {
    public ArrayList<Module> modules = new ArrayList();
    public List<Module> sortedModules = new ArrayList<Module>();
    public List<Module> alphabeticallySortedModules = new ArrayList<Module>();
    public Animation animationThread;

    public void init() {

        //CHAT
      modules.add(new AntiVanish());

      //CLIENT
      modules.add(new ClickGui());
      modules.add(new Colors());
      modules.add(new HudEditor());
      modules.add(new MainMenu());
      modules.add(new ClientControl());

      //COMBAT
      modules.add(new AutoArmor());
      modules.add(new AutoCrystalNew());
      modules.add(new PacketEXP());
      modules.add(new AutoMinecart());
      modules.add(new AutoTotem());
      modules.add(new AutoTrap());
      modules.add(new Crasher());
      modules.add(new Criticals());
      modules.add(new CrystalAura());
      modules.add(new Killaura());
      modules.add(new AutoCrystal());
      modules.add(new Offhand());
      modules.add(new Selftrap());
      modules.add(new Surround());

     //MISC
      modules.add(new AutoLog());
      modules.add(new AutoReconnect());
      modules.add(new AutoRespawn());
      modules.add(new BuildHeight());
      modules.add(new ExtraTab());
      modules.add(new MiddleClickFriends());
      modules.add(new MobOwner());
      modules.add(new NoRotate());
      modules.add(new NoSoundLag());
      modules.add(new OffhandCrash());
      modules.add(new PacketCanceller());
      modules.add(new PacketLogger());
      modules.add(new PearlNotify());
      modules.add(new PingSpoof());

      //MOVEMENT
      modules.add(new Anchor());
      modules.add(new AntiLevitate());
      modules.add(new AutoWalk());
      modules.add(new FastSwim());
      modules.add(new LongJump());
      modules.add(new NoFall());
      modules.add(new NoSlowDown());
      modules.add(new NoVoid());
      modules.add(new PacketFly());
      modules.add(new ReverseStep());
      modules.add(new SafeWalk());
      modules.add(new Speed());
      modules.add(new Sprint());
      modules.add(new Step());
      modules.add(new Velocity());

      //PLAYER
      modules.add(new AntiHunger());
      modules.add(new BlockTweaks());
      modules.add(new Burrow());
      modules.add(new FakePlayer());
      modules.add(new FastPlace());
      modules.add(new Freecam());
      modules.add(new Jesus());
      modules.add(new LiquidInteract());
      modules.add(new PearlThrow());
      modules.add(new Scaffold());
      modules.add(new Speedmine());
      modules.add(new XCarry());

      //RENDER
      modules.add(new BlockHighlight());
      modules.add(new CameraClip());
      modules.add(new Chams());
      modules.add(new ChildESP());
      modules.add(new ColorChams());
      modules.add(new ESP());
      modules.add(new EnchantColor());
      modules.add(new Fullbright());
      modules.add(new GlowESP());
      modules.add(new HoleESP());
      modules.add(new ItemPhysics());
      modules.add(new Nametags());
      modules.add(new NoRender());
      modules.add(new SmallShield());
      modules.add(new StorageESP());


      this.animationThread = new Animation();
      this.animationThread.start();
    }

    public Module getModuleByName(String name) {
        Module module;
        Iterator<Module> iterator = this.modules.iterator();
        do {
            if (!iterator.hasNext()) return null;
        } while (!(module = iterator.next()).getName().equalsIgnoreCase(name));
        return module;
    }

    @Deprecated
    public Module getModuleByNameDep(String name) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public <T extends Module> T getModule(Class<T> clazz) {
        Module module;
        Iterator<Module> iterator = this.modules.iterator();
        do {
            if (!iterator.hasNext()) return null;
        } while (!clazz.isInstance(module = iterator.next()));
        return (T)module;
    }

    public void enableModule(Class clazz) {
        Module module = this.getModule(clazz);
        if (module == null) return;
        ((Module)module).enable();
    }

    public void disableModule(Class clazz) {
        Module module = this.getModule(clazz);
        if (module == null) return;
        ((Module)module).disable();
    }

    public void enableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module == null) return;
        module.enable();
    }

    public void disableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module == null) return;
        module.disable();
    }

    public boolean isModuleEnabled(String name) {
        Module module = this.getModuleByName(name);
        if (module == null) return false;
        if (!module.isOn()) return false;
        return true;
    }

    public boolean isModuleEnabled(Class clazz) {
        Module module = this.getModule(clazz);
        if (module == null) return false;
        if (!((Module)module).isOn()) return false;
        return true;
    }

    public Module getModuleByDisplayName(String displayName) {
        Module module;
        Iterator<Module> iterator = this.modules.iterator();
        do {
            if (!iterator.hasNext()) return null;
        } while (!(module = iterator.next()).getDisplayName().equalsIgnoreCase(displayName));
        return module;
    }

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> enabledModules = new ArrayList<Module>();
        Iterator<Module> iterator = this.modules.iterator();
        while (iterator.hasNext()) {
            Module module = iterator.next();
            if (!module.isEnabled() && !module.isSliding()) continue;
            enabledModules.add(module);
        }
        return enabledModules;
    }

    public ArrayList<Module> getModulesByCategory(Module.Category category) {
        ArrayList<Module> modulesCategory = new ArrayList<Module>();
        this.modules.forEach(module -> {
            if (module.getCategory() != category) return;
            modulesCategory.add((Module)module);
        });
        return modulesCategory;
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onLoad() {
        this.modules.stream().filter(Module::listening).forEach(((EventBus)MinecraftForge.EVENT_BUS)::register);
        this.modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        this.modules.stream().filter(Hack::isEnabled).forEach(Module::onUpdate);
    }

    public void onTick() {
        this.modules.stream().filter(Hack::isEnabled).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        this.modules.stream().filter(Hack::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        this.modules.stream().filter(Hack::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public static void onRender() {
        MoonHack.getModuleManager.modules.stream().filter(Hack::isEnabled).forEach(Module::onRender);
    }

    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> {
            int n;
            if (reverse) {
                n = -1;
                return this.renderer.getStringWidth(module.getFullArrayString()) * n;
            }
            n = 1;
            return this.renderer.getStringWidth(module.getFullArrayString()) * n;
        })).collect(Collectors.toList());
    }

    public void alphabeticallySortModules() {
        this.alphabeticallySortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(Module::getDisplayName)).collect(Collectors.toList());
    }

    public void onLogout() {
        this.modules.forEach(Module::onLogout);
    }

    public void onLogin() {
        this.modules.forEach(Module::onLogin);
    }

    public void onUnload() {
        this.modules.forEach(((EventBus)MinecraftForge.EVENT_BUS)::unregister);
        this.modules.forEach(Module::onUnload);
    }

    public void onUnloadPost() {
        Iterator<Module> iterator = this.modules.iterator();
        while (iterator.hasNext()) {
            Module module = iterator.next();
            module.enabled.setValue(false);
        }
    }

    public void onKeyPressed(int eventKey) {
        if (eventKey == 0) return;
        if (!Keyboard.getEventKeyState()) return;
        if (ModuleManager.mc.currentScreen instanceof MoonHackGui) {
            return;
        }
        this.modules.forEach(module -> {
            if (module.getBind().getKey() != eventKey) return;
            module.toggle();
        });
    }

    private class Animation
            extends Thread {
        ScheduledExecutorService service;
        public Module module;
        public float offset;
        public float vOffset;

        public Animation() {
            super("Animation");
            this.service = Executors.newSingleThreadScheduledExecutor();
        }

        public void run() {
            Iterator moonModuleTime = ModuleManager.this.sortedModules.iterator();

            while(true) {
                while(moonModuleTime.hasNext()) {
                    Module module = (Module)moonModuleTime.next();
                    String text = module.getDisplayName() + "§7" + (module.getDisplayInfo() != null ? " [§f" + module.getDisplayInfo() + "§7" + "]" : "");
                }

                return;
            }
        }

        public void start() {
            System.out.println("Starting animation thread.");
            this.service.scheduleAtFixedRate(this, 1L, 5L, TimeUnit.MILLISECONDS);
        }
    }
}

