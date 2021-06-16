package com.ragemachine.moon.impl.client.modules.render;

import com.ragemachine.moon.api.event.events.Render3DEvent;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.util.RenderUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.awt.*;

public class BlockHighlight extends Module {

    public Value<Boolean> box = addSetting(new Value("Box", false));
    public Value<Boolean> outline = addSetting(new Value("Outline", true));
    private final Value<Integer> red = addSetting(new Value("Red", 0, 0, 255));
    private final Value<Integer> green = addSetting(new Value("Green", 255, 0, 255));
    private final Value<Integer> blue = addSetting(new Value("Blue", 0, 0, 255));
    private final Value<Integer> alpha = addSetting(new Value("Alpha", 255, 0, 255));
    private final Value<Integer> boxAlpha = addSetting(new Value("BoxAlpha", 125, 0, 255, v -> box.getValue()));
    private final Value<Float> lineWidth = addSetting(new Value("LineWidth", 1.0f, 0.1f, 5.0f, v -> outline.getValue()));
    public Value<Boolean> customOutline = addSetting(new Value("CustomLine", false, v -> outline.getValue()));
    private final Value<Integer> cRed = addSetting(new Value("OL-Red", 255, 0, 255, v -> customOutline.getValue() && outline.getValue()));
    private final Value<Integer> cGreen = addSetting(new Value("OL-Green", 255, 0, 255, v -> customOutline.getValue() && outline.getValue()));
    private final Value<Integer> cBlue = addSetting(new Value("OL-Blue", 255, 0, 255, v -> customOutline.getValue() && outline.getValue()));
    private final Value<Integer> cAlpha = addSetting(new Value("OL-Alpha", 255, 0, 255, v -> customOutline.getValue() && outline.getValue()));

    public BlockHighlight() {
        super("BlockHighlight", "Highlights the block u look at.", Category.RENDER, false, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        RayTraceResult ray = mc.objectMouseOver;
        if(ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = ray.getBlockPos();
            RenderUtil.drawBoxESP(blockpos, new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()), customOutline.getValue(), new Color(cRed.getValue(), cGreen.getValue(), cBlue.getValue(), cAlpha.getValue()), lineWidth.getValue(), outline.getValue(), box.getValue(), boxAlpha.getValue(), false);
        }
    }
}
