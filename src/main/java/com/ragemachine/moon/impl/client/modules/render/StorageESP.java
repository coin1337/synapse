package com.ragemachine.moon.impl.client.modules.render;

import java.awt.Color;
import java.util.concurrent.ConcurrentHashMap;

import com.ragemachine.moon.api.event.events.Render3DEvent;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.SynapseTessellator;
import net.minecraft.tileentity.*;

public class StorageESP extends Module {
    ConcurrentHashMap chests = new ConcurrentHashMap();

    public StorageESP() {
        super("StorageESP", "Highlight chests and stuff", Module.Category.RENDER, true, false, true);
    }

    public Value<Integer> a = addSetting(new Value<>("Alpha", 150, 0, 255));
    public Value<Float> w = addSetting(new Value<>("Width", 1.0f, 1.0f, 10.0f));

    @Override
    public void onUpdate() {
        mc.world.loadedTileEntityList.forEach((e) -> {
            this.chests.put(e, "");
        });
    }


    @Override
    public void onRender3D(Render3DEvent event) {
        Color c1 = new Color(200, 100, 0, this.a.getValue());
        Color c2 = new Color(200, 0, 200, this.a.getValue());
        Color c3 = new Color(150, 150, 150, this.a.getValue());
        if (this.chests != null && this.chests.size() > 0) {
            SynapseTessellator.prepareGL();
            this.chests.forEach((c, t) -> {
                if (mc.world.loadedTileEntityList.contains(c)) {
                    if (c instanceof TileEntityChest || c instanceof TileEntityShulkerBox) {
                        SynapseTessellator.drawBoundingBox(mc.world.getBlockState(((TileEntityLockableLoot) c).getPos()).getSelectedBoundingBox(mc.world, ((TileEntityLockableLoot) c).getPos()), this.w.getValue(), c1.getRGB());
                    }

                    if (c instanceof TileEntityEnderChest) {
                        SynapseTessellator.drawBoundingBox(mc.world.getBlockState(((TileEntityEnderChest) c).getPos()).getSelectedBoundingBox(mc.world, ((TileEntityEnderChest) c).getPos()), this.w.getValue(), c2.getRGB());
                    }

                    if (c instanceof TileEntityDispenser || c instanceof TileEntityFurnace || c instanceof TileEntityHopper) {
                        SynapseTessellator.drawBoundingBox(mc.world.getBlockState(((TileEntityLockable) c).getPos()).getSelectedBoundingBox(mc.world, ((TileEntityLockable) c).getPos()), this.w.getValue(), c3.getRGB());
                    }
                }

            });
            SynapseTessellator.releaseGL();
        }

    }
}
