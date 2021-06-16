package com.ragemachine.moon.impl.client.modules.combat;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.api.event.events.UpdateWalkingPlayerEvent;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.DamageUtil;
import com.ragemachine.moon.impl.util.EntityUtil;
import com.ragemachine.moon.impl.util.MathUtil;
import com.ragemachine.moon.impl.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Killaura extends Module {

    public Value<Float> range = addSetting(new Value("Range", 6.0f, 0.1f, 7.0f));
    private Value<TargetMode> targetMode = addSetting(new Value("Target", TargetMode.CLOSEST));
    public Value<Float> health = addSetting(new Value("Health", 6.0f, 0.1f, 36.0f, v -> targetMode.getValue() == TargetMode.SMART));
    public Value<Boolean> delay = addSetting(new Value("Delay", true));
    public Value<Boolean> rotate = addSetting(new Value("Rotate", true));
    public Value<Boolean> armorBreak = addSetting(new Value("ArmorBreak", false));
    public Value<Boolean> eating = addSetting(new Value("Eating", true));
    public Value<Boolean> onlySharp = addSetting(new Value("Axe/Sword", true));
    public Value<Boolean> teleport = addSetting(new Value("Teleport", false));
    public Value<Float> raytrace = addSetting(new Value("Raytrace", 6.0f, 0.1f, 7.0f, v -> !teleport.getValue(), "Wall Range."));
    public Value<Float> teleportRange = addSetting(new Value("TpRange", 15.0f, 0.1f, 50.0f, v -> teleport.getValue(), "Teleport Range."));
    public Value<Boolean> lagBack = addSetting(new Value("LagBack", true, v -> teleport.getValue()));
    public Value<Boolean> teekaydelay = addSetting(new Value("32kDelay", false));
    public Value<Integer> time32k = addSetting(new Value("32kTime", 5, 1, 50));
    public Value<Integer> multi = addSetting(new Value("32kPackets", 2, v -> !teekaydelay.getValue()));
    public Value<Boolean> multi32k = addSetting(new Value("Multi32k", false));
    public Value<Boolean> players = addSetting(new Value("Players", true));
    public Value<Boolean> mobs = addSetting(new Value("Mobs", false));
    public Value<Boolean> animals = addSetting(new Value("Animals", false));
    public Value<Boolean> vehicles = addSetting(new Value("Entities", false));
    public Value<Boolean> projectiles = addSetting(new Value("Projectiles", false));
    public Value<Boolean> tps = addSetting(new Value("TpsSync", true));
    public Value<Boolean> packet = addSetting(new Value("Packet", false));
    public Value<Boolean> info = addSetting(new Value("Info", true));

    private final Timer timer = new Timer();
    public static Entity target;

    public Killaura() {
        super("Killaura", "Kills aura.", Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        if(!rotate.getValue()) {
            doKillaura();
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayerEvent(UpdateWalkingPlayerEvent event) {
        if(event.getStage() == 0 && rotate.getValue()) {
            doKillaura();
        }
    }

    private void doKillaura() {
        if(onlySharp.getValue() && !EntityUtil.holdingWeapon(mc.player)) {
            target = null;
            return;
        }

        int wait = (!delay.getValue() || (EntityUtil.holding32k(mc.player) && !teekaydelay.getValue())) ? 0 : (int)(DamageUtil.getCooldownByWeapon(mc.player) * (tps.getValue() ? MoonHack.getServerManager.getTpsFactor() : 1));
        if(!timer.passedMs(wait) || (!eating.getValue() && mc.player.isHandActive() && !(mc.player.getHeldItemOffhand().getItem().equals(Items.SHIELD) && mc.player.getActiveHand() == EnumHand.OFF_HAND))) {
            return;
        }

        if(!(targetMode.getValue() == TargetMode.FOCUS && target != null && ((mc.player.getDistanceSq(target) < MathUtil.square(range.getValue()) || (teleport.getValue() && mc.player.getDistanceSq(target) < MathUtil.square(teleportRange.getValue()))) && ((mc.player.canEntityBeSeen(target) || EntityUtil.canEntityFeetBeSeen(target) || mc.player.getDistanceSq(target) < MathUtil.square(raytrace.getValue())) || teleport.getValue())))) {
            target = getTarget();
        }

        if(target == null) {
            return;
        }

        if(rotate.getValue()) {
            MoonHack.getRotationManager.lookAtEntity(target);
        }

        if(teleport.getValue()) {
            MoonHack.getPositionManager.setPositionPacket(target.posX, EntityUtil.canEntityFeetBeSeen(target) ? target.posY : target.posY + target.getEyeHeight(), target.posZ, true, true, !lagBack.getValue());
        }

        if(EntityUtil.holding32k(mc.player) && !teekaydelay.getValue()) {
            if(multi32k.getValue()) {
                for(EntityPlayer player : mc.world.playerEntities) {
                    if(EntityUtil.isValid(player, range.getValue())) {
                        teekayAttack(player);
                    }
                }
            } else {
                teekayAttack(target);
            }
            timer.reset();
            return;
        }

        if(armorBreak.getValue()) {
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 9, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
            EntityUtil.attackEntity(target, packet.getValue(), true);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 9, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
            EntityUtil.attackEntity(target, packet.getValue(), true);
        } else {
            EntityUtil.attackEntity(target, packet.getValue(), true);
        }
        timer.reset();
    }

    private void teekayAttack(Entity entity) {
        for(int i = 0; i < multi.getValue(); i++) {
            //EntityUtil.attackEntity(entity, packet.getMoonButtonVal(), true);
            startEntityAttackThread(entity, i * time32k.getValue());
        }
    }

    private void startEntityAttackThread(Entity entity, int time) {
        new Thread(() -> {
            Timer timer = new Timer();
            timer.reset();
            try {
                Thread.sleep(time);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            EntityUtil.attackEntity(entity, true, true);
        }).start();
    }

    private Entity getTarget() {
        Entity target = null;
        double distance = teleport.getValue() ? teleportRange.getValue() : range.getValue();
        double maxHealth = 36.0;
        for(Entity entity : mc.world.loadedEntityList) {
            if((players.getValue() && entity instanceof EntityPlayer) || (animals.getValue() && EntityUtil.isPassive(entity)) || (mobs.getValue() && EntityUtil.isMobAggressive(entity)) || (vehicles.getValue() && EntityUtil.isVehicle(entity)) || (projectiles.getValue() && EntityUtil.isProjectile(entity))) {
                if(entity instanceof EntityLivingBase && EntityUtil.isntValid(entity, distance)) {
                    continue;
                }

                if (!teleport.getValue() && !mc.player.canEntityBeSeen(entity) && !EntityUtil.canEntityFeetBeSeen(entity) && mc.player.getDistanceSq(entity) > MathUtil.square(raytrace.getValue())) {
                    continue;
                }

                if(target == null) {
                    target = entity;
                    distance = mc.player.getDistanceSq(entity);
                    maxHealth = EntityUtil.getHealth(entity);
                    continue;
                }

                if(entity instanceof EntityPlayer && DamageUtil.isArmorLow((EntityPlayer)entity, 18)) {
                    target = entity;
                    break;
                }

                if(targetMode.getValue() == TargetMode.SMART && EntityUtil.getHealth(entity) < health.getValue()) {
                    target = entity;
                    break;
                }

                if(targetMode.getValue() != TargetMode.HEALTH && mc.player.getDistanceSq(entity) < distance) {
                    target = entity;
                    distance = mc.player.getDistanceSq(entity);
                    maxHealth = EntityUtil.getHealth(entity);
                }

                if (targetMode.getValue() == TargetMode.HEALTH && EntityUtil.getHealth(entity) < maxHealth) {
                    target = entity;
                    distance = mc.player.getDistanceSq(entity);
                    maxHealth = EntityUtil.getHealth(entity);
                }
            }
        }
        return target;
    }

    @Override
    public String getDisplayInfo() {
        if(info.getValue() && target != null && target instanceof EntityPlayer)  {
            return target.getName();
        }
        return null;
    }

    public enum TargetMode {
        FOCUS,
        CLOSEST,
        HEALTH,
        SMART
    }
}
