package com.ragemachine.moon.impl.client.modules.player;

import com.ragemachine.moon.impl.client.modules.Module;

public class LiquidInteract extends Module {

    //T

    private static LiquidInteract INSTANCE = new LiquidInteract();

    public LiquidInteract() {
        super("LiquidInteract", "Interact with liquids", Category.PLAYER, false, false, false);
        setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static LiquidInteract getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new LiquidInteract();
        }
        return INSTANCE;
    }
}
