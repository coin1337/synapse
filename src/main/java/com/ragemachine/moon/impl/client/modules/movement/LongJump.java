

package com.ragemachine.moon.impl.client.modules.movement;

import com.ragemachine.moon.api.event.events.PlayerMoveEvent;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class LongJump extends Module {
    private static /* synthetic */ String[] llIlIIl;
    private static /* synthetic */ int[] llIllII;
    private /* synthetic */ double motionSpeed;
    private /* synthetic */ int currentState;
    @EventHandler
    private /* synthetic */ Listener<PlayerMoveEvent> packetEventListener;
    private /* synthetic */ double prevDist;

    Value<Double> multiplier = addSetting(new Value("Multiplier",  4.1, 1.0, 10.0));
    Value<Boolean> autoSprint = addSetting(new Value("Auto Sprint",  false));
    Value<Boolean> accelerationTimer = addSetting(new Value("Acceleration Timer",  false));
    Value<Integer> timerSpeed = addSetting(new Value("Timer Speed",  1, 0, 10));
    Value<Boolean> speedDetect = addSetting(new Value("Speed Detect",  true));
    Value<Boolean> jumpDetect = addSetting(new Value("Leaping Detect",  true));
    Value<Double> extraYBoost = addSetting(new Value("Extra Y Boost",  0.0, 0.0, 1.0));


    private boolean attempting = false;

    public LongJump() {
        super("LongJump", "hop around", Category.MOVEMENT, true, false, true);
    }

   /*    @Override
    public void onEnable() {
        attempting = false;
    }

    @Override
    public void onDisable() {
    }


    static {
        lIIlIIIII();
        lIIIllllI();
    }

    private static void lIIlIIIII() {
        (llIllII = new int[10])[0] = ((0xD5 ^ 0x80) & ~(0x6 ^ 0x53));
        LongJump.llIllII[1] = " ".length();
        LongJump.llIllII[2] = "  ".length();
        LongJump.llIllII[3] = "   ".length();
        LongJump.llIllII[4] = (0xB0 ^ 0xBA);
        LongJump.llIllII[5] = (0x14 ^ 0x7D ^ (0x4D ^ 0x20));
        LongJump.llIllII[6] = (57 + 30 + 7 + 36 ^ 134 + 15 - 64 + 50);
        LongJump.llIllII[7] = (0xD5 ^ 0x8D ^ (0x3F ^ 0x61));
        LongJump.llIllII[8] = (0x51 ^ 0x11 ^ (0x4A ^ 0xD));
        LongJump.llIllII[9] = (71 + 178 - 231 + 172 ^ 69 + 149 - 198 + 162);
    }


    private static int lIIlIIIIl(float var0, float var1) {
        float var2;
        return (var2 = var0 - var1) == 0.0F ? 0 : (var2 < 0.0F ? -1 : 1);
    }


    private static boolean lIIlIlIII(final int lllIIlIIlIIIlll) {
        return lllIIlIIlIIIlll <= 0;
    }

    private static int lIIlIIllI(float var0, float var1) {
        float var2;
        return (var2 = var0 - var1) == 0.0F ? 0 : (var2 < 0.0F ? -1 : 1);
    }


    private static boolean lIIlIlIIl(final int lllIIlIIlIIIlIl) {
        return lllIIlIIlIIIlIl > 0;
    }

    @Override
    public void onUpdate() {
        if (isNull(LongJump.mc.player)) {
            return;
        }
        this.prevDist = Math.sqrt((LongJump.mc.player.posX - LongJump.mc.player.prevPosX) * (LongJump.mc.player.posX - LongJump.mc.player.prevPosX) + (LongJump.mc.player.posZ - LongJump.mc.player.prevPosZ) * (LongJump.mc.player.posZ - LongJump.mc.player.prevPosZ));
        if (lIIlIIlII(((boolean)this.accelerationTimer.getValue()) ? 1 : 0)) {
            LongJump.mc.timer.tickLength = 50.0f / this.timerSpeed.getValue();
        }
        else if (lIIlIIlII(lIIlIIIIl(LongJump.mc.timer.tickLength, 50.0f))) {
            LongJump.mc.timer.tickLength = 50.0f;
        }
        if (lIIlIIlIl(LongJump.mc.player.isSprinting() ? 1 : 0) && lIIlIIlII(((boolean)this.autoSprint.getValue()) ? 1 : 0)) {
            LongJump.mc.player.setSprinting((boolean)(LongJump.llIllII[1] != 0));
            LongJump.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)LongJump.mc.player, CPacketEntityAction.Action.START_SPRINTING));
        }
    }

    private static int lIIlIIlll(double var0, double var2) {
        double var4;
        return (var4 = var0 - var2) == 0.0D ? 0 : (var4 < 0.0D ? -1 : 1);
    }


    private static boolean isNull(final Object lllIIlIIlIIllIl) {
        return lllIIlIIlIIllIl == null;
    }

    private static boolean lIIlIIlII(final int lllIIlIIlIIlIll) {
        return lllIIlIIlIIlIll != 0;
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        double lllIIlIIlllIlII;
        double lllIIlIIlllIIIl;
        double lllIIlIIlllIIII;
        double lllIIlIIllIllll;
        if (!isNull(LongJump.mc.player)) {
            //test
            float currentTps = mc.timer.tickLength / 1000.0f;
            switch (this.currentState) {
                case 0: {
                    this.currentState += LongJump.llIllII[1];
                    this.prevDist = 0.0;
                    break;
                }
                case 2: {
                    lllIIlIIlllIlII = 0.40123128 + this.extraYBoost.getValue();
                    if ((!lIIlIIlIl(lIIlIIllI(LongJump.mc.player.moveForward, 0.0f)) || lIIlIIlII(lIIlIIllI(LongJump.mc.player.moveStrafing, 0.0f))) && lIIlIIlII(LongJump.mc.player.onGround ? 1 : 0)) {
                        if (lIIlIIlII(LongJump.mc.player.isPotionActive(MobEffects.JUMP_BOOST) ? 1 : 0) && lIIlIIlII(((boolean)this.jumpDetect.getValue()) ? 1 : 0)) {
                            lllIIlIIlllIlII += (LongJump.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + LongJump.llIllII[1]) * 0.1f;
                        }
                        event.setY(mc.player.motionY = lllIIlIIlllIlII);
                        this.motionSpeed *= 2.149;
                        break;
                    }
                    else {
                        break;
                    }
                }
                case 3: {
                    this.motionSpeed = this.prevDist - 0.76 * (this.prevDist - this.getBaseMotionSpeed());
                    break;
                }
                default: {
                    if ((!lIIlIlIII(LongJump.mc.world.getCollisionBoxes((Entity)LongJump.mc.player, LongJump.mc.player.getEntityBoundingBox().offset(0.0, LongJump.mc.player.motionY, 0.0)).size()) || lIIlIIlII(LongJump.mc.player.collidedVertically ? 1 : 0)) && lIIlIlIIl(this.currentState)) {
                        if (lIIlIIlIl(lIIlIIllI(LongJump.mc.player.moveForward, 0.0f)) && lIIlIIlIl(lIIlIIllI(LongJump.mc.player.moveStrafing, 0.0f))) {
                            currentState = LongJump.llIllII[0];
                        }
                        else {
                            currentState = LongJump.llIllII[1];
                        }
                    }
                    this.motionSpeed = this.prevDist - this.prevDist / 159.0;
                    break;
                }
            }
            this.motionSpeed = Math.max(this.motionSpeed, this.getBaseMotionSpeed());
            lllIIlIIlllIIIl = LongJump.mc.player.movementInput.moveForward;
            lllIIlIIlllIIII = LongJump.mc.player.movementInput.moveStrafe;
            lllIIlIIllIllll = LongJump.mc.player.rotationYaw;
            if (lIIlIIlIl(lIIlIIlll(lllIIlIIlllIIIl, 0.0)) && lIIlIIlIl(lIIlIIlll(lllIIlIIlllIIII, 0.0))) {
                event.setX(0.0);
                event.setZ(0.0);
            }
            if (lIIlIIlII(lIIlIIlll(lllIIlIIlllIIIl, 0.0)) && lIIlIIlII(lIIlIIlll(lllIIlIIlllIIII, 0.0))) {
                lllIIlIIlllIIIl *= Math.sin(0.7853981633974483);
                lllIIlIIlllIIII *= Math.cos(0.7853981633974483);
            }
            event.setX((lllIIlIIlllIIIl * this.motionSpeed * -Math.sin(Math.toRadians(lllIIlIIllIllll)) + lllIIlIIlllIIII * this.motionSpeed * Math.cos(Math.toRadians(lllIIlIIllIllll))) * (1 * 0.99));
            event.setZ((lllIIlIIlllIIIl * this.motionSpeed * Math.cos(Math.toRadians(lllIIlIIllIllll)) - lllIIlIIlllIIII * this.motionSpeed * -Math.sin(Math.toRadians(lllIIlIIllIllll))) * (1 * 0.99));
            attempting = true;
            this.currentState += LongJump.llIllII[1];
        }
        event.setCanceled(true);
    }

    private static void lIIIllllI() {
        (llIlIIl = new String[LongJump.llIllII[8]])[LongJump.llIllII[0]] = lIIIllIII("n9pHF6SFvkOs6iUr+fnXgA==", "GmCTC");
        LongJump.llIlIIl[LongJump.llIllII[1]] = lIIIllIII("4noHmwJ5F40+cu8qBPcyzA==", "CVFaT");
        LongJump.llIlIIl[LongJump.llIllII[2]] = lIIIllIII("R+hGwU+dCgQQcUdIkD9ZYaUO+QBhMxiN", "RjGgZ");
        LongJump.llIlIIl[LongJump.llIllII[3]] = lIIIllIII("Dk9SQuIPQSn5I8lWMj8Z+w==", "dWNML");
        LongJump.llIlIIl[LongJump.llIllII[5]] = lIIIllIII("rPWGh7vSeiSJWWJOJQfq5wdZ8fI6Y9G+", "QkkkG");
        LongJump.llIlIIl[LongJump.llIllII[6]] = lIIIllIII("6BSD78RsHX6yVgm/4JINjBgTGCxZfgXF", "rXpxu");
        LongJump.llIlIIl[LongJump.llIllII[7]] = lIIIlllIl("ENR8rJxJYtA86kRMf8iVlQ==", "RTxXY");
    }

    private double getBaseMotionSpeed() {
        double lllIIlIIllllllI = 0.272 * this.multiplier.getValue();
        if (lIIlIIlII(LongJump.mc.player.isPotionActive(MobEffects.SPEED) ? 1 : 0) && lIIlIIlII(((boolean)this.speedDetect.getValue()) ? 1 : 0)) {
            final int lllIIlIlIIIIIII = Objects.requireNonNull(LongJump.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
            lllIIlIIllllllI *= 1.0 + 0.2 * lllIIlIlIIIIIII;
        }
        return lllIIlIIllllllI;
    }

    private static boolean lIIlIIlIl(final int lllIIlIIlIIlIIl) {
        return lllIIlIIlIIlIIl == 0;
    }

    private static String lIIIllIII(final String lllIIlIIlIlllll, final String lllIIlIIllIIIII) {
        try {
            final SecretKeySpec lllIIlIIllIIlII = new SecretKeySpec(MessageDigest.getInstance("MD5").digest(lllIIlIIllIIIII.getBytes(StandardCharsets.UTF_8)), "Blowfish");
            final Cipher lllIIlIIllIIIll = Cipher.getInstance("Blowfish");
            lllIIlIIllIIIll.init(LongJump.llIllII[2], lllIIlIIllIIlII);
            return new String(lllIIlIIllIIIll.doFinal(Base64.getDecoder().decode(lllIIlIIlIlllll.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);
        }
        catch (Exception lllIIlIIllIIIlI) {
            lllIIlIIllIIIlI.printStackTrace();
            return null;
        }
    }

    private static String lIIIlllIl(final String lllIIlIIlIlIlII, final String lllIIlIIlIlIIIl) {
        try {
            final SecretKeySpec lllIIlIIlIlIlll = new SecretKeySpec(Arrays.copyOf(MessageDigest.getInstance("MD5").digest(lllIIlIIlIlIIIl.getBytes(StandardCharsets.UTF_8)), LongJump.llIllII[9]), "DES");
            final Cipher lllIIlIIlIlIllI = Cipher.getInstance("DES");
            lllIIlIIlIlIllI.init(LongJump.llIllII[2], lllIIlIIlIlIlll);
            return new String(lllIIlIIlIlIllI.doFinal(Base64.getDecoder().decode(lllIIlIIlIlIlII.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);
        }
        catch (Exception lllIIlIIlIlIlIl) {
            lllIIlIIlIlIlIl.printStackTrace();
            return null;
        }
    }*/
}
