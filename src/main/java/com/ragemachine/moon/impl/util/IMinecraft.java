package com.ragemachine.moon.impl.util;

import net.minecraft.util.Timer;

public interface IMinecraft {
   Timer getTimer();

   void setRightClickDelayTimer(int var1);
}
