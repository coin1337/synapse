

package com.ragemachine.moon.impl.client.modules.render;


import com.ragemachine.moon.impl.client.modules.Module;

public class Chams extends Module
{
    private static Chams INSTANCE;
    
    public Chams() {
        super("Chams", "Renders players through walls.", Category.RENDER, false, false, false);
        this.setInstance();
    }
    
    private void setInstance() {
        Chams.INSTANCE = this;
    }
    
    public static Chams getInstance() {
        if (Chams.INSTANCE == null) {
            Chams.INSTANCE = new Chams();
        }
        return Chams.INSTANCE;
    }
    
    static {
        Chams.INSTANCE = new Chams();
    }
}
