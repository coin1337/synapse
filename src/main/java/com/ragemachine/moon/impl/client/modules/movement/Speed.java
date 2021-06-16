

package com.ragemachine.moon.impl.client.modules.movement;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.api.event.events.MoveEvent;
import com.ragemachine.moon.api.event.events.PacketEvent;
import com.ragemachine.moon.api.event.events.UpdateWalkingPlayerEvent;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.EntityUtil;
import com.ragemachine.moon.impl.util.Timer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Speed extends Module
{
    private final Value<Mode> mode;
    private final Value<Boolean> limiter;
    private final Value<Boolean> bhop2;
    private final Value<Boolean> limiter2;
    private final Value<Boolean> noLag;
    private final Value<Integer> specialMoveSpeed;
    private final Value<Integer> potionSpeed;
    private final Value<Integer> potionSpeed2;
    private final Value<Integer> dFactor;
    private final Value<Integer> acceleration;
    private final Value<Float> speedLimit;
    private final Value<Float> speedLimit2;
    private final Value<Integer> yOffset;
    private final Value<Boolean> potion;
    private final Value<Boolean> wait;
    private final Value<Boolean> hopWait;
    private final Value<Integer> startStage;
    private final Value<Boolean> setPos;
    private final Value<Boolean> setNull;
    private final Value<Integer> setGroundLimit;
    private final Value<Integer> groundFactor;
    private final Value<Integer> step;
    private int stage;
    private double moveSpeed;
    private double lastDist;
    private int cooldownHops;
    private boolean waitForGround;
    private Timer timer;
    private int hops;

    public Speed() {
        super("Speed", "AirControl etc.", Category.MOVEMENT, true, false, false);
        this.mode = (Value<Mode>)this.addSetting(new Value("Mode", Mode.STRICT));
        this.limiter = (Value<Boolean>)this.addSetting(new Value("SetGround", true));
        this.bhop2 = (Value<Boolean>)this.addSetting(new Value("Hop", true));
        this.limiter2 = (Value<Boolean>)this.addSetting(new Value("Bhop", false));
        this.noLag = (Value<Boolean>)this.addSetting(new Value("NoLag", false));
        this.specialMoveSpeed = (Value<Integer>)this.addSetting(new Value("Speed", 100, 0, 150));
        this.potionSpeed = (Value<Integer>)this.addSetting(new Value("Speed1", 130, 0, 150));
        this.potionSpeed2 = (Value<Integer>)this.addSetting(new Value("Speed2", 125, 0, 150));
        this.dFactor = (Value<Integer>)this.addSetting(new Value("DFactor", 159, 100, 200));
        this.acceleration = (Value<Integer>)this.addSetting(new Value("Accel", 2149, 1000, 2500));
        this.speedLimit = (Value<Float>)this.addSetting(new Value("SpeedLimit", 35.0f, 20.0f, 60.0f));
        this.speedLimit2 = (Value<Float>)this.addSetting(new Value("SpeedLimit2", 60.0f, 20.0f, 60.0f));
        this.yOffset = (Value<Integer>)this.addSetting(new Value("YOffset", 400, 350, 500));
        this.potion = (Value<Boolean>)this.addSetting(new Value("Potion", false));
        this.wait = (Value<Boolean>)this.addSetting(new Value("Wait", true));
        this.hopWait = (Value<Boolean>)this.addSetting(new Value("HopWait", true));
        this.startStage = (Value<Integer>)this.addSetting(new Value("Stage", 2, 0, 4));
        this.setPos = (Value<Boolean>)this.addSetting(new Value("SetPos", true));
        this.setNull = (Value<Boolean>)this.addSetting(new Value("SetNull", false));
        this.setGroundLimit = (Value<Integer>)this.addSetting(new Value("GroundLimit", 138, 0, 1000));
        this.groundFactor = (Value<Integer>)this.addSetting(new Value("GroundFactor", 13, 0, 50));
        this.step = (Value<Integer>)this.addSetting(new Value("SetStep", 1, 0, 2, v -> this.mode.getValue() == Mode.BHOP));
        this.stage = 1;
        this.cooldownHops = 0;
        this.waitForGround = false;
        this.timer = new Timer();
        this.hops = 0;
    }

    @Override
    public void onEnable() {
        if (!Speed.mc.player.onGround) {
            this.waitForGround = true;
        }
        this.hops = 0;
        this.timer.reset();
        this.moveSpeed = getBaseMoveSpeed();
    }

    @Override
    public void onDisable() {
        this.hops = 0;
        this.moveSpeed = 0.0;
        this.stage = this.startStage.getValue();
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0) {
            this.lastDist = Math.sqrt((Speed.mc.player.posX - Speed.mc.player.prevPosX) * (Speed.mc.player.posX - Speed.mc.player.prevPosX) + (Speed.mc.player.posZ - Speed.mc.player.prevPosZ) * (Speed.mc.player.posZ - Speed.mc.player.prevPosZ));
        }
    }

    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (event.getStage() != 0) {
            return;
        }
        if (!Speed.mc.player.onGround) {
            if (this.wait.getValue() && this.waitForGround) {
                return;
            }
        }
        else {
            this.waitForGround = false;
        }
        if (this.mode.getValue() == Mode.STRICT) {
            this.doNCP(event);
        }
        else if (this.mode.getValue() == Mode.BHOP) {
            float moveForward = Speed.mc.player.movementInput.moveForward;
            float moveStrafe = Speed.mc.player.movementInput.moveStrafe;
            float rotationYaw = Speed.mc.player.rotationYaw;
            if (this.step.getValue() == 1) {
                Speed.mc.player.stepHeight = 0.6f;
            }
            if (this.limiter2.getValue() && Speed.mc.player.onGround && MoonHack.getSpeedManager.getSpeedKpH() < this.speedLimit2.getValue()) {
                this.stage = 2;
            }
            if (this.limiter.getValue() && round(Speed.mc.player.posY - (int)Speed.mc.player.posY, 3) == round(this.setGroundLimit.getValue() / 1000.0, 3)) {
                if (this.setNull.getValue()) {
                    Speed.mc.player.motionY = 0.0;
                }
                else {
                    final EntityPlayerSP player = Speed.mc.player;
                    player.motionY -= this.groundFactor.getValue() / 100.0;
                    event.setY(event.getY() - this.groundFactor.getValue() / 100.0);
                    if (this.setPos.getValue()) {
                        final EntityPlayerSP player2 = Speed.mc.player;
                        player2.posY -= this.groundFactor.getValue() / 100.0;
                    }
                }
            }
            if (this.stage == 1 && EntityUtil.isMoving()) {
                this.stage = 2;
                this.moveSpeed = this.getMultiplier() * getBaseMoveSpeed() - 0.01;
            }
            else if (this.stage == 2 && EntityUtil.isMoving()) {
                this.stage = 3;
                Speed.mc.player.motionY = this.yOffset.getValue() / 1000.0;
                event.setY(this.yOffset.getValue() / 1000.0);
                if (this.cooldownHops > 0) {
                    --this.cooldownHops;
                }
                ++this.hops;
                this.moveSpeed *= this.acceleration.getValue() / 1000.0;
            }
            else if (this.stage == 3) {
                this.stage = 4;
                final double difference = 0.66 * (this.lastDist - getBaseMoveSpeed());
                this.moveSpeed = this.lastDist - difference;
            }
            else {
                if (Speed.mc.world.getCollisionBoxes((Entity)Speed.mc.player, Speed.mc.player.getEntityBoundingBox().offset(0.0, Speed.mc.player.motionY, 0.0)).size() > 0 || (Speed.mc.player.collidedVertically && this.stage > 0)) {
                    if (this.bhop2.getValue() && MoonHack.getSpeedManager.getSpeedKpH() >= this.speedLimit.getValue()) {
                        this.stage = 0;
                    }
                    else {
                        this.stage = ((Speed.mc.player.moveForward != 0.0f || Speed.mc.player.moveStrafing != 0.0f) ? 1 : 0);
                    }
                }
                this.moveSpeed = this.lastDist - this.lastDist / this.dFactor.getValue();
            }
            this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
            if (this.hopWait.getValue() && this.limiter2.getValue() && this.hops < 2) {
                this.moveSpeed = EntityUtil.getMaxSpeed();
            }
            if (moveForward == 0.0f && moveStrafe == 0.0f) {
                event.setX(0.0);
                event.setZ(0.0);
                this.moveSpeed = 0.0;
            }
            else if (moveForward != 0.0f) {
                if (moveStrafe >= 1.0f) {
                    rotationYaw += ((moveForward > 0.0f) ? -45.0f : 45.0f);
                    moveStrafe = 0.0f;
                }
                else if (moveStrafe <= -1.0f) {
                    rotationYaw += ((moveForward > 0.0f) ? 45.0f : -45.0f);
                    moveStrafe = 0.0f;
                }
                if (moveForward > 0.0f) {
                    moveForward = 1.0f;
                }
                else if (moveForward < 0.0f) {
                    moveForward = -1.0f;
                }
            }
            final double motionX = Math.cos(Math.toRadians(rotationYaw + 90.0f));
            final double motionZ = Math.sin(Math.toRadians(rotationYaw + 90.0f));
            if (this.cooldownHops == 0) {
                event.setX(moveForward * this.moveSpeed * motionX + moveStrafe * this.moveSpeed * motionZ);
                event.setZ(moveForward * this.moveSpeed * motionZ - moveStrafe * this.moveSpeed * motionX);
            }
            if (this.step.getValue() == 2) {
                Speed.mc.player.stepHeight = 0.6f;
            }
            if (moveForward == 0.0f && moveStrafe == 0.0f) {
                this.timer.reset();
                event.setX(0.0);
                event.setZ(0.0);
            }
        }
    }

    private void doNCP(final MoveEvent event) {
        if (!this.limiter.getValue() && Speed.mc.player.onGround) {
            this.stage = 2;
        }
        switch (this.stage) {
            case 0: {
                ++this.stage;
                this.lastDist = 0.0;
                break;
            }
            case 2: {
                double motionY = 0.40123128;
                if ((Speed.mc.player.moveForward != 0.0f || Speed.mc.player.moveStrafing != 0.0f) && Speed.mc.player.onGround) {
                    if (Speed.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                        motionY += (Speed.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1f;
                    }
                    event.setY(Speed.mc.player.motionY = motionY);
                    this.moveSpeed *= 2.149;
                    break;
                }
                break;
            }
            case 3: {
                this.moveSpeed = this.lastDist - 0.76 * (this.lastDist - getBaseMoveSpeed());
                break;
            }
            default: {
                if (Speed.mc.world.getCollisionBoxes((Entity)Speed.mc.player, Speed.mc.player.getEntityBoundingBox().offset(0.0, Speed.mc.player.motionY, 0.0)).size() > 0 || (Speed.mc.player.collidedVertically && this.stage > 0)) {
                    if (this.bhop2.getValue() && MoonHack.getSpeedManager.getSpeedKpH() >= this.speedLimit.getValue()) {
                        this.stage = 0;
                    }
                    else {
                        this.stage = ((Speed.mc.player.moveForward != 0.0f || Speed.mc.player.moveStrafing != 0.0f) ? 1 : 0);
                    }
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                break;
            }
        }
        this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
        double forward = Speed.mc.player.movementInput.moveForward;
        double strafe = Speed.mc.player.movementInput.moveStrafe;
        final double yaw = Speed.mc.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        }
        else if (forward != 0.0 && strafe != 0.0) {
            forward *= Math.sin(0.7853981633974483);
            strafe *= Math.cos(0.7853981633974483);
        }
        event.setX((forward * this.moveSpeed * -Math.sin(Math.toRadians(yaw)) + strafe * this.moveSpeed * Math.cos(Math.toRadians(yaw))) * 0.99);
        event.setZ((forward * this.moveSpeed * Math.cos(Math.toRadians(yaw)) - strafe * this.moveSpeed * -Math.sin(Math.toRadians(yaw))) * 0.99);
        ++this.stage;
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.272;
        if (Speed.mc.player.isPotionActive(MobEffects.SPEED)) {
            final int amplifier = Objects.requireNonNull(Speed.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * amplifier;
        }
        return baseSpeed;
    }

    private float getMultiplier() {
        float baseSpeed = this.specialMoveSpeed.getValue();
        if (this.potion.getValue() && Speed.mc.player.isPotionActive(MobEffects.SPEED)) {
            final int amplifier = Objects.requireNonNull(Speed.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier() + 1;
            if (amplifier >= 2) {
                baseSpeed = this.potionSpeed2.getValue();
            }
            else {
                baseSpeed = this.potionSpeed.getValue();
            }
        }
        return baseSpeed / 100.0f;
    }


    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook && this.noLag.getValue()) {
            if (this.mode.getValue() == Mode.BHOP && (this.limiter2.getValue() || this.bhop2.getValue())) {
                this.stage = 1;
            }
            else {
                this.stage = 4;
            }
        }
    }

    @Override
    public String getDisplayInfo() {
        if (this.mode.getValue() == Mode.NONE) {
            return null;
        }
        if (this.mode.getValue() == Mode.STRICT) {
            return this.mode.currentEnumName().toUpperCase();
        }
        return this.mode.currentEnumName();
    }

    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        final BigDecimal bigDecimal = new BigDecimal(value).setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public enum Mode
    {
        NONE,
        STRICT,
        BHOP;
    }
}
