package com.ragemachine.moon.impl.client.modules.combat;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.gui.MoonHackGui;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.modules.player.XCarry;
import com.ragemachine.moon.impl.client.setting.Bind;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.DamageUtil;
import com.ragemachine.moon.impl.util.EntityUtil;
import com.ragemachine.moon.impl.util.InventoryUtil;
import com.ragemachine.moon.impl.util.MathUtil;
import com.ragemachine.moon.impl.util.Timer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.ragemachine.moon.impl.util.InventoryUtil.Task;

public class AutoArmor extends Module {

    private final Value<Integer> delay = addSetting(new Value("Delay", 50, 0, 500));
    private final Value<Boolean> mendingTakeOff = addSetting(new Value("AutoMend", false));
    private final Value<Integer> closestEnemy = addSetting(new Value("Enemy", 8, 1, 20, v -> mendingTakeOff.getValue()));
    private final Value<Integer> helmetThreshold = addSetting(new Value("Helmet%", 80, 1, 100, v -> mendingTakeOff.getValue()));
    private final Value<Integer> chestThreshold = addSetting(new Value("Chest%", 80, 1, 100, v -> mendingTakeOff.getValue()));
    private final Value<Integer> legThreshold = addSetting(new Value("Legs%", 80, 1, 100, v -> mendingTakeOff.getValue()));
    private final Value<Integer> bootsThreshold = addSetting(new Value("Boots%", 80, 1, 100, v -> mendingTakeOff.getValue()));
    private final Value<Boolean> curse = addSetting(new Value("Cursed Armor", false));
    private final Value<Bind> elytraBind = addSetting(new Value("Elytra", new Bind(-1)));
    private final Value<Boolean> tps = addSetting(new Value("TpsSync", true));
    private final Value<Boolean> shiftClick = addSetting(new Value("ShiftClick", false));

    private final Timer timer = new Timer();
    private final Timer elytraTimer = new Timer();
    private final Queue<Task> taskList = new ConcurrentLinkedQueue<>();
    private final List<Integer> doneSlots = new ArrayList<>();
    private boolean elytraOn = false;

    public AutoArmor() {
        super("AutoArmor", "Puts Armor on for you.", Category.COMBAT, true, false, false);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(Keyboard.getEventKeyState() && !(mc.currentScreen instanceof MoonHackGui) && elytraBind.getValue().getKey() == Keyboard.getEventKey()) {
            elytraOn = !elytraOn;
        }
    }

    @Override
    public void onLogin() {
        timer.reset();
        elytraTimer.reset();
    }

    @Override
    public void onDisable() {
        taskList.clear();
        doneSlots.clear();
        elytraOn = false;
    }

    @Override
    public void onLogout() {
        taskList.clear();
        doneSlots.clear();
    }

    @Override
    public void onTick() {
        if(fullNullCheck() || (mc.currentScreen instanceof GuiContainer && !(mc.currentScreen instanceof GuiInventory))) {
            return;
        }

        if(taskList.isEmpty()) {
            if (mendingTakeOff.getValue() && InventoryUtil.holdingItem(ItemExpBottle.class) && mc.gameSettings.keyBindUseItem.isKeyDown() && (isSafe() || EntityUtil.isSafe(mc.player, 1, false))) {
                final ItemStack helm = mc.player.inventoryContainer.getSlot(5).getStack();
                if (!helm.isEmpty) {
                    int helmDamage = DamageUtil.getRoundedDamage(helm);
                    if (helmDamage >= helmetThreshold.getValue()) {
                        takeOffSlot(5);
                    }
                }

                final ItemStack chest = mc.player.inventoryContainer.getSlot(6).getStack();
                if (!chest.isEmpty) {
                    int chestDamage = DamageUtil.getRoundedDamage(chest);
                    if (chestDamage >= chestThreshold.getValue()) {
                        takeOffSlot(6);
                    }
                }

                final ItemStack legging = mc.player.inventoryContainer.getSlot(7).getStack();
                if (!legging.isEmpty) {
                    int leggingDamage = DamageUtil.getRoundedDamage(legging);
                    if (leggingDamage >= legThreshold.getValue()) {
                        takeOffSlot(7);
                    }
                }

                final ItemStack feet = mc.player.inventoryContainer.getSlot(8).getStack();
                if (!feet.isEmpty) {
                    int bootDamage = DamageUtil.getRoundedDamage(feet);
                    if (bootDamage >= bootsThreshold.getValue()) {
                        takeOffSlot(8);
                    }
                }
                return;
            }

            final ItemStack helm = mc.player.inventoryContainer.getSlot(5).getStack();
            if (helm.getItem() == Items.AIR) {
                final int slot = InventoryUtil.findArmorSlot(EntityEquipmentSlot.HEAD, curse.getValue(), XCarry.getInstance().isOn());
                if (slot != -1) {
                    getSlotOn(5, slot);
                }
            }

            final ItemStack chest = mc.player.inventoryContainer.getSlot(6).getStack();
            if (chest.getItem() == Items.AIR) {
                if (taskList.isEmpty()) {
                    if (elytraOn && elytraTimer.passedMs(500)) {
                        int elytraSlot = InventoryUtil.findItemInventorySlot(Items.ELYTRA, false, XCarry.getInstance().isOn());
                        if (elytraSlot != -1) {
                            if((elytraSlot < 5 && elytraSlot > 1) || !shiftClick.getValue()) {
                                taskList.add(new Task(elytraSlot));
                                taskList.add(new Task(6));
                            } else {
                                taskList.add(new Task(elytraSlot, true));
                            }
                            taskList.add(new Task());
                            elytraTimer.reset();
                        }
                    } else if (!elytraOn) {
                        final int slot = InventoryUtil.findArmorSlot(EntityEquipmentSlot.CHEST, curse.getValue(), XCarry.getInstance().isOn());
                        if (slot != -1) {
                            getSlotOn(6, slot);
                        }
                    }
                }
            } else {
                if (elytraOn && chest.getItem() != Items.ELYTRA && elytraTimer.passedMs(500)) {
                    if (taskList.isEmpty()) {
                        final int slot = InventoryUtil.findItemInventorySlot(Items.ELYTRA, false, XCarry.getInstance().isOn());
                        if(slot != -1) {
                            taskList.add(new Task(slot));
                            taskList.add(new Task(6));
                            taskList.add(new Task(slot));
                            taskList.add(new Task());
                        }
                        elytraTimer.reset();
                    }
                } else if (!elytraOn && chest.getItem() == Items.ELYTRA && elytraTimer.passedMs(500) && taskList.isEmpty()) {
                    //TODO: WTF IS THIS
                    int slot = InventoryUtil.findItemInventorySlot(Items.DIAMOND_CHESTPLATE, false, XCarry.getInstance().isOn());
                    if (slot == -1) {
                        slot = InventoryUtil.findItemInventorySlot(Items.IRON_CHESTPLATE, false, XCarry.getInstance().isOn());
                        if (slot == -1) {
                            slot = InventoryUtil.findItemInventorySlot(Items.GOLDEN_CHESTPLATE, false, XCarry.getInstance().isOn());
                            if (slot == -1) {
                                slot = InventoryUtil.findItemInventorySlot(Items.CHAINMAIL_CHESTPLATE, false, XCarry.getInstance().isOn());
                                if (slot == -1) {
                                    slot = InventoryUtil.findItemInventorySlot(Items.LEATHER_CHESTPLATE, false, XCarry.getInstance().isOn());
                                }
                            }
                        }
                    }

                    if (slot != -1) {
                        taskList.add(new Task(slot));
                        taskList.add(new Task(6));
                        taskList.add(new Task(slot));
                        taskList.add(new Task());
                    }
                    elytraTimer.reset();
                }
            }

            final ItemStack legging = mc.player.inventoryContainer.getSlot(7).getStack();
            if (legging.getItem() == Items.AIR) {
                final int slot = InventoryUtil.findArmorSlot(EntityEquipmentSlot.LEGS, curse.getValue(), XCarry.getInstance().isOn());
                if (slot != -1) {
                    getSlotOn(7, slot);
                }
            }

            final ItemStack feet = mc.player.inventoryContainer.getSlot(8).getStack();
            if (feet.getItem() == Items.AIR) {
                final int slot = InventoryUtil.findArmorSlot(EntityEquipmentSlot.FEET, curse.getValue(), XCarry.getInstance().isOn());
                if (slot != -1) {
                    getSlotOn(8, slot);
                }
            }
        }

        if(timer.passedMs((int)(delay.getValue() * (tps.getValue() ? MoonHack.getServerManager.getTpsFactor() : 1)))) {
            if (!taskList.isEmpty()) {
                for (int i = 0; i < 3; i++) {
                    Task task = taskList.poll();
                    if (task != null) {
                        task.run();
                    }
                }
            }
            timer.reset();
        }
    }

    @Override
    public String getDisplayInfo() {
        if(elytraOn) {
            return "Elytra";
        } else {
            return null;
        }
    }

    private void takeOffSlot(int slot) {
        if(taskList.isEmpty()) {
            int target = -1;
            for(int i : InventoryUtil.findEmptySlots(XCarry.getInstance().isOn())) {
                if (!doneSlots.contains(target)) {
                    target = i;
                    doneSlots.add(i);
                }
            }

            if (target != -1) {
                if((target < 5 && target > 0) || !shiftClick.getValue()) {
                    taskList.add(new Task(slot));
                    taskList.add(new Task(target));
                } else {
                    taskList.add(new Task(slot, true));
                }
                taskList.add(new Task());
            }
        }
    }

    private void getSlotOn(int slot, int target) {
        if(taskList.isEmpty()) {
            doneSlots.remove((Object)target);
            if((target < 5 && target > 0) || !shiftClick.getValue()) {
                taskList.add(new Task(target));
                taskList.add(new Task(slot));
            } else {
                taskList.add(new Task(target, true));
            }
            taskList.add(new Task());
        }
    }

    private boolean isSafe() {
        EntityPlayer closest = EntityUtil.getClosestEnemy(closestEnemy.getValue());
        if(closest == null) {
            return true;
        }
        return mc.player.getDistanceSq(closest) >= MathUtil.square(closestEnemy.getValue());
    }

}
