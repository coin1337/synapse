package com.ragemachine.moon.impl.client.modules.render;

import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import scala.Enumeration;

public class ItemPhysics extends Module {
    //Thanks Apolo Client


    private static ItemPhysics INSTANCE = new ItemPhysics();

    public Value<Float> scaling = addSetting(new Value("Scale",0.5f, 0f, 10f));

    public ItemPhysics() {
        super("ItemPhysics", "Adds physics to dropped items", Category.RENDER, true, false, true);

        setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static ItemPhysics getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ItemPhysics();
        return INSTANCE;
    }
}
