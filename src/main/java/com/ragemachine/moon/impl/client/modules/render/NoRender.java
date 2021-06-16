package com.ragemachine.moon.impl.client.modules.render;

import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static net.minecraft.client.gui.GuiBossOverlay.GUI_BARS_TEXTURES;

public class NoRender extends Module {

    public Value<Boolean> fire = addSetting(new Value("Fire", false, "Removes the portal overlay."));
    public Value<Boolean> portal = addSetting(new Value("Portal", false, "Removes the portal overlay."));
    public Value<Boolean> pumpkin = addSetting(new Value("Pumpkin", false, "Removes the pumpkin overlay."));
    public Value<Boolean> totemPops = addSetting(new Value("TotemPop", false, "Removes the Totem overlay."));
    public Value<Boolean> items = addSetting(new Value("Items", false, "Removes items on the ground."));
    public Value<Boolean> nausea = addSetting(new Value("Nausea", false, "Removes Portal Nausea."));
    public Value<Boolean> hurtcam = addSetting(new Value("HurtCam", false, "Removes shaking after taking damage."));
    public Value<Fog> fog = addSetting(new Value("Fog", Fog.NONE, "Removes Fog."));
    public Value<Boolean> noWeather = addSetting(new Value("Weather", false, "AntiWeather"));
    public Value<Boss> boss = addSetting(new Value("BossBars", Boss.NONE, "Modifies the bossbars."));
    public Value<Float> scale = addSetting(new Value("Scale", 0.0f, 0.5f, 1.0f, v -> boss.getValue() == Boss.MINIMIZE || boss.getValue() != Boss.STACK, "Scale of the bars."));
    public Value<Boolean> bats = addSetting(new Value("Bats", false, "Removes bats."));
    public Value<NoArmor> noArmor = addSetting(new Value("NoArmor", NoArmor.NONE, "Doesnt Render Armor on players."));
    public Value<Skylight> skylight = addSetting(new Value("Skylight", Skylight.NONE));
    public Value<Boolean> barriers = addSetting(new Value("Barriers", false, "Barriers"));

    public enum Skylight {
        NONE,
        WORLD,
        ENTITY,
        ALL
    }

    private static NoRender INSTANCE = new NoRender();

    public NoRender() {
        super("NoRender", "Allows you to stop rendering stuff", Category.RENDER, true, false, false);
        setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if(items.getValue()) {
            mc.world.loadedEntityList.stream().filter(EntityItem.class::isInstance).map(EntityItem.class::cast).forEach(Entity::setDead);
        }

        if (noWeather.getValue() && mc.world.isRaining()) {
            mc.world.setRainStrength(0);
        }
    }

    public void doVoidFogParticles(int posX, int posY, int posZ)
    {
        int i = 32;
        Random random = new Random();
        ItemStack itemstack = mc.player.getHeldItemMainhand();
        boolean flag = !barriers.getValue() || (mc.playerController.getCurrentGameType() == GameType.CREATIVE && !itemstack.isEmpty() && itemstack.getItem() == Item.getItemFromBlock(Blocks.BARRIER));
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int j = 0; j < 667; ++j)
        {
            this.showBarrierParticles(posX, posY, posZ, 16, random, flag, blockpos$mutableblockpos);
            this.showBarrierParticles(posX, posY, posZ, 32, random, flag, blockpos$mutableblockpos);
        }
    }

    public void showBarrierParticles(int x, int y, int z, int offset, Random random, boolean holdingBarrier, BlockPos.MutableBlockPos pos)
    {
        int i = x + mc.world.rand.nextInt(offset) - mc.world.rand.nextInt(offset);
        int j = y + mc.world.rand.nextInt(offset) - mc.world.rand.nextInt(offset);
        int k = z + mc.world.rand.nextInt(offset) - mc.world.rand.nextInt(offset);
        pos.setPos(i, j, k);
        IBlockState iblockstate = mc.world.getBlockState(pos);
        iblockstate.getBlock().randomDisplayTick(iblockstate, mc.world, pos, random);

        if (!holdingBarrier && iblockstate.getBlock() == Blocks.BARRIER)
        {
            mc.world.spawnParticle(EnumParticleTypes.BARRIER, (double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    @SubscribeEvent
    public void onRenderPre(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO && boss.getValue() != Boss.NONE) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRenderPost(RenderGameOverlayEvent.Post event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO && boss.getValue() != Boss.NONE) {
            if (boss.getValue() == Boss.MINIMIZE) {
                Map<UUID, BossInfoClient> map = mc.ingameGUI.getBossOverlay().mapBossInfos;
                if (map == null) return;
                ScaledResolution scaledresolution = new ScaledResolution(mc);
                int i = scaledresolution.getScaledWidth();
                int j = 12;
                for (Map.Entry<UUID, BossInfoClient> entry : map.entrySet()) {
                    BossInfoClient info = entry.getValue();
                    String text = info.getName().getFormattedText();
                    int k = (int) ((i / scale.getValue()) / 2 - 91);
                    GL11.glScaled(scale.getValue(), scale.getValue(), 1);
                    if (!event.isCanceled()) {
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                        mc.getTextureManager().bindTexture(GUI_BARS_TEXTURES);
                        mc.ingameGUI.getBossOverlay().render(k, j, info);
                        mc.fontRenderer.drawStringWithShadow(text, (float) ((i / scale.getValue()) / 2 - mc.fontRenderer.getStringWidth(text) / 2), (float) (j - 9), 16777215);
                    }
                    GL11.glScaled(1d / scale.getValue(), 1d / scale.getValue(), 1);
                    j += 10 + mc.fontRenderer.FONT_HEIGHT;
                }
            } else if (boss.getValue() == Boss.STACK) {
                Map<UUID, BossInfoClient> map = mc.ingameGUI.getBossOverlay().mapBossInfos;
                HashMap<String, Pair<BossInfoClient, Integer>> to = new HashMap<>();
                for (Map.Entry<UUID, BossInfoClient> entry : map.entrySet()) {
                    String s = entry.getValue().getName().getFormattedText();
                    if (to.containsKey(s)) {
                        Pair<BossInfoClient, Integer> p = to.get(s);
                        p = new Pair<>(p.getKey(), p.getValue() + 1);
                        to.put(s, p);
                    } else {
                        Pair<BossInfoClient, Integer> p = new Pair<>(entry.getValue(), 1);
                        to.put(s, p);
                    }
                }
                ScaledResolution scaledresolution = new ScaledResolution(mc);
                int i = scaledresolution.getScaledWidth();
                int j = 12;
                for (Map.Entry<String, Pair<BossInfoClient, Integer>> entry : to.entrySet()) {
                    String text = entry.getKey();
                    BossInfoClient info = entry.getValue().getKey();
                    int a = entry.getValue().getValue();
                    text += " x" + a;
                    int k = (int) ((i / scale.getValue()) / 2 - 91);
                    GL11.glScaled(scale.getValue(), scale.getValue(), 1);
                    if (!event.isCanceled()) {
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                        mc.getTextureManager().bindTexture(GUI_BARS_TEXTURES);
                        mc.ingameGUI.getBossOverlay().render(k, j, info);
                        mc.fontRenderer.drawStringWithShadow(text, (float) ((i / scale.getValue()) / 2 - mc.fontRenderer.getStringWidth(text) / 2), (float) (j - 9), 16777215);
                    }
                    GL11.glScaled(1d / scale.getValue(), 1d / scale.getValue(), 1);
                    j += 10 + mc.fontRenderer.FONT_HEIGHT;
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Pre<?> event) {
        if(bats.getValue() && event.getEntity() instanceof EntityBat) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onBlockOverlay(RenderBlockOverlayEvent event) {
        if (fire.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE) event.setCanceled(true);
    }

    @SubscribeEvent
    public void onPlaySound(PlaySoundAtEntityEvent event) {
        if(bats.getValue() && event.getSound().equals(SoundEvents.ENTITY_BAT_AMBIENT)
                || event.getSound().equals(SoundEvents.ENTITY_BAT_DEATH)
                || event.getSound().equals(SoundEvents.ENTITY_BAT_HURT)
                || event.getSound().equals(SoundEvents.ENTITY_BAT_LOOP)
                || event.getSound().equals(SoundEvents.ENTITY_BAT_TAKEOFF)) {
            event.setVolume(0.f);
            event.setPitch(0.f);
            event.setCanceled(true);
        }
    }

    public static NoRender getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new NoRender();
        }
        return INSTANCE;
    }

    public enum Fog {
        NONE,
        AIR,
        NOFOG
    }

    public enum Boss {
        NONE,
        REMOVE,
        STACK,
        MINIMIZE
    }

    public enum NoArmor {
        NONE,
        ALL,
        HELMET
    }

    public static class Pair<T, S> {

        private T key;
        private S value;

        public Pair(T key, S value) {
            this.key = key;
            this.value = value;
        }

        public T getKey() {
            return key;
        }

        public S getValue() {
            return value;
        }

        public void setKey(T key) {
            this.key = key;
        }

        public void setValue(S value) {
            this.value = value;
        }
    }
}
