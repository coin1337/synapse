package com.ragemachine.moon.impl.client.modules.render;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.api.event.events.Render3DEvent;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.BlockUtil;
import com.ragemachine.moon.impl.util.MathUtil;
import com.ragemachine.moon.impl.util.RenderUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class HoleESP extends Module {

    public Value<Integer> range = addSetting(new Value("Range", 3, 1, 50));
    public Value<Boolean> box = addSetting(new Value("Box", true));
    public Value<Boolean> outline = addSetting(new Value("Outline", true));
    public Value<Float> holeHeight = addSetting(new Value("Hole Height",1f, 0.01f, 2f));
    private Value<Integer> red = addSetting(new Value("Obby Red", 0, 0, 255));
    private Value<Integer> green = addSetting(new Value("Obby Green", 255, 0, 255));
    private Value<Integer> blue = addSetting(new Value("Obby Blue", 0, 0, 255));
    private Value<Integer> alpha = addSetting(new Value("Obby Alpha", 255, 0, 255));
    private Value<Integer> boxAlpha = addSetting(new Value("BoxAlpha", 125, 0, 255, v -> box.getValue()));
    private Value<Float> lineWidth = addSetting(new Value("LineWidth", 1.0f, 0.1f, 5.0f, v -> outline.getValue()));
    private Value<Integer> safeRed = addSetting(new Value("Bedrock Red", 0, 0, 255));
    private Value<Integer> safeGreen = addSetting(new Value("Bedrock Green", 255, 0, 255));
    private Value<Integer> safeBlue = addSetting(new Value("Bedrock Blue", 0, 0, 255));
    private Value<Integer> safeAlpha = addSetting(new Value("Bedrock Alpha", 255, 0, 255));
    private Value<Mode> mode = addSetting(new Value("Mode", Mode.BEACON));
    private Value<Boolean> small = addSetting(new Value("Small Beacon", false, v -> mode.getValue() == Mode.BEACON));
    private static HoleESP INSTANCE = new HoleESP();

    public HoleESP() {
        super("HoleESP", "Shows safe spots.", Category.RENDER, false, false, false);
        setInstance();
    }

    public enum Mode {
        OLD,
        BEACON;
    }

    private void setInstance() {
        INSTANCE = this;
    }



    public static HoleESP getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new HoleESP();
        }
        return INSTANCE;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        int drawnHoles = 0;
        for (BlockPos pos : MoonHack.HOLE_MANAGER.getSortedHoles()) {

            if (pos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))) {
                continue;
            }

            if (mode.getValue() == Mode.OLD) {
                    if (MoonHack.HOLE_MANAGER.isSafe(pos)) {
                        RenderUtil.drawBoxESP(pos, new Color(safeRed.getValue(), safeGreen.getValue(), safeBlue.getValue(), safeAlpha.getValue()), false, new Color(safeRed.getValue(), safeGreen.getValue(), safeBlue.getValue(), safeAlpha.getValue()), lineWidth.getValue(), outline.getValue(), box.getValue(), boxAlpha.getValue(), true, holeHeight.getValue() -1);
                    } else {
                        RenderUtil.drawBoxESP(pos, new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()), false, new Color(red.getValue(), red.getValue(), red.getValue(), red.getValue()), lineWidth.getValue(), outline.getValue(), box.getValue(), boxAlpha.getValue(), true, holeHeight.getValue() -1);
                    }
                    drawnHoles++;
            }


            if (mode.getValue() == Mode.BEACON) {
                if (MoonHack.HOLE_MANAGER.isSafe(pos)) {
                    this.drawBlockIndicator(pos, safeRed.getValue(), safeGreen.getValue(), safeBlue.getValue());
                }
                if (MoonHack.HOLE_MANAGER.isSafe(pos) == false) {
                    this.drawBlockIndicator(pos, red.getValue(), green.getValue(), blue.getValue());
                }
            }
        }
    }


    private void drawBlockIndicator(final BlockPos blockPos, final int r, final int g, final int b) {
        final int red = (r);
        final int green = (g);
        final int blue = (b);
        final IBlockState iBlockState2 = mc.world.getBlockState(blockPos);
        final Vec3d interp2 = MathUtil.interpolateEntity(mc.player, mc.getRenderPartialTicks());
        AxisAlignedBB aabb = iBlockState2.getSelectedBoundingBox(mc.world, blockPos);
        if (small.getValue() == false) {
            aabb = aabb.setMaxY(aabb.maxY + (mc.player.getDistanceSq(blockPos) < 1 ? 0 : 3)).grow(0.0020000000949949026D).offset(-interp2.x, -interp2.y, -interp2.z);
        } else {
            aabb = aabb.setMaxY(aabb.maxY + (mc.player.getDistanceSq(blockPos) < 300 ? 0 : 3)).grow(0.0020000000949949026D).offset(-interp2.x, -interp2.y, -interp2.z);
        }
        RenderUtil.drawIndicator(aabb, new Color(red, green, blue, alpha.getValue()).getRGB(), 63);
    }
}
