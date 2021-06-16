

package com.ragemachine.moon.impl.client.modules.player;

import com.mojang.authlib.GameProfile;
import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FakePlayer extends Module
{

    /*
    * TEMPORARY CODE
    * rn its from phobos cause im lazy, imma rewrite it to be a command
     */
    private Value<Boolean> copyInv;
    public Value<Boolean> multi;
    private Value<Integer> players;
    private static final String[] fitInfo;
    public static final String[][] moonhackInfo;
    public List<Integer> fakePlayerIdList;
    private final List<EntityOtherPlayerMP> fakeEntities;
    private static FakePlayer INSTANCE;

    public FakePlayer() {
        super("FakePlayer", "Spawns in a fake player", Category.PLAYER, true, false, false);
        this.copyInv = (Value<Boolean>)this.addSetting(new Value("CopyInv", true));
        this.multi = (Value<Boolean>)this.addSetting(new Value("Multi", false));
        this.players = (Value<Integer>)this.addSetting(new Value("Players", 1, 1, 9, v -> this.multi.getValue(), "Amount of other players."));
        this.fakePlayerIdList = new ArrayList<Integer>();
        this.fakeEntities = new ArrayList<EntityOtherPlayerMP>();
        this.setInstance();
    }

    private void setInstance() {
        FakePlayer.INSTANCE = this;
    }

    public static FakePlayer getInstance() {
        if (FakePlayer.INSTANCE == null) {
            FakePlayer.INSTANCE = new FakePlayer();
        }
        return FakePlayer.INSTANCE;
    }

    @Override
    public void onLoad() {
        this.disable();
    }

    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            this.disable();
            return;
        }
        this.fakePlayerIdList = new ArrayList<Integer>();
        if (this.multi.getValue()) {
            int amount = 0;
            int entityId = -101;
            for (final String[] data : FakePlayer.moonhackInfo) {
                this.addFakePlayer(data[0], data[1], entityId, Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                if (++amount >= this.players.getValue()) {
                    return;
                }
                entityId -= amount;
            }
        }
        else {
            this.addFakePlayer(FakePlayer.fitInfo[0], FakePlayer.fitInfo[1], -100, 0, 0);
        }
    }

    @Override
    public void onDisable() {
        if (fullNullCheck()) {
            return;
        }
        for (final int id : this.fakePlayerIdList) {
            FakePlayer.mc.world.removeEntityFromWorld(id);
        }
    }

    @Override
    public void onLogout() {
        if (this.isOn()) {
            this.disable();
        }
    }

    private void addFakePlayer(final String uuid, final String name, final int entityId, final int offsetX, final int offsetZ) {
        final GameProfile profile = new GameProfile(UUID.fromString(uuid), name);
        final EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP((World)FakePlayer.mc.world, profile);
        fakePlayer.copyLocationAndAnglesFrom((Entity)FakePlayer.mc.player);
        fakePlayer.posX += offsetX;
        fakePlayer.posZ += offsetZ;
        if (this.copyInv.getValue()) {
            for (final PotionEffect potionEffect : MoonHack.POTION_MANAGER.getOwnPotions()) {
                fakePlayer.addPotionEffect(potionEffect);
            }
            fakePlayer.inventory.copyInventory(FakePlayer.mc.player.inventory);
        }
        fakePlayer.setHealth(FakePlayer.mc.player.getHealth() + FakePlayer.mc.player.getAbsorptionAmount());
        this.fakeEntities.add(fakePlayer);
        FakePlayer.mc.world.addEntityToWorld(entityId, (Entity)fakePlayer);
        this.fakePlayerIdList.add(entityId);
    }

    static {
        fitInfo = new String[] { "6bc610b8-7fa8-4938-bf89-fb0b105bec12", "RageMachine222" };
        moonhackInfo = new String[][] { { "8af022c8-b926-41a0-8b79-2b544ff00fcf", "3arthqu4ke", "3", "0" }, { "0aa3b04f-786a-49c8-bea9-025ee0dd1e85", "zb0b", "-3", "0" }, { "19bf3f1f-fe06-4c86-bea5-3dad5df89714", "3vt", "0", "-3" }, { "e47d6571-99c2-415b-955e-c4bc7b55941b", "Moon Hack_eu", "0", "3" }, { "b01f9bc1-cb7c-429a-b178-93d771f00926", "bakpotatisen", "6", "0" }, { "b232930c-c28a-4e10-8c90-f152235a65c5", "948", "-6", "0" }, { "ace08461-3db3-4579-98d3-390a67d5645b", "Browswer", "0", "-6" }, { "5bead5b0-3bab-460d-af1d-7929950f40c2", "fsck", "0", "6" }, { "78ee2bd6-64c4-45f0-96e5-0b6747ba7382", "Fit", "0", "9" }, { "78ee2bd6-64c4-45f0-96e5-0b6747ba7382", "deathcurz0", "0", "-9" } };
        FakePlayer.INSTANCE = new FakePlayer();
    }
}
