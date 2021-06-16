

package com.ragemachine.moon.api.event;


import com.ragemachine.moon.impl.util.Wrapper;

public class EclientEvent
{
    private Era era;
    private final float partialTicks;
    
    public EclientEvent() {
        this.era = Era.PRE;
        this.partialTicks = Wrapper.getMinecraft().getRenderPartialTicks();
    }
    
    public Era getEra() {
        return this.era;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
    
    public enum Era
    {
        PRE, 
        PERI, 
        POST;
    }
}
