package com.ragemachine.moon.impl.manager;

import com.ragemachine.moon.impl.client.Hack;
import com.ragemachine.moon.impl.util.TextUtil;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TotemPopManager extends Hack {


    private Map<EntityPlayer, Integer> poplist = new ConcurrentHashMap<>();
    private Set<EntityPlayer> toAnnounce = new HashSet<>();

    public void onUpdate() {
        for (EntityPlayer player : toAnnounce) {
            if (player == null) {
                continue;
            }
        }
    }


    public void onTotemPop(EntityPlayer player) {
        popTotem(player);
        if(!player.equals(mc.player)) {
            toAnnounce.add(player);
        }
    }

    public void onDeath(EntityPlayer player) {
        resetPops(player);
    }

    public void onLogout(EntityPlayer player, boolean clearOnLogout) {
        if(clearOnLogout) {
            resetPops(player);
        }
    }

    public void onOwnLogout(boolean clearOnLogout) {
        if(clearOnLogout) {
            clearList();
        }
    }

    public void clearList() {
        poplist = new ConcurrentHashMap<>();
    }

    public void resetPops(EntityPlayer player) {
        setTotemPops(player, 0);
    }

    public void popTotem(EntityPlayer player) {
        poplist.merge(player, 1, Integer::sum);
    }

    public void setTotemPops(EntityPlayer player, int amount) {
        poplist.put(player, amount);
    }

    public int getTotemPops(EntityPlayer player) {
        Integer pops = poplist.get(player);
        if(pops == null) {
            return 0;
        }
        return pops;
    }

    public String getTotemPopString(EntityPlayer player) {
        return TextUtil.WHITE + (getTotemPops(player) <= 0 ? "" : "-" + getTotemPops(player) + " ");
    }
}
