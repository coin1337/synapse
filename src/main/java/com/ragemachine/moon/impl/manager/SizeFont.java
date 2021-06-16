

package com.ragemachine.moon.impl.manager;

import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;

public class SizeFont extends Module
{
/* TESTING
* New Module organization and value adding system
* Possible custom font mod in future when I have time to make it
 */

    public Value<Integer> fontSize;

    public SizeFont() {
        super("TESTING SOMETHING", "TEST", Category.CLIENT, true, true, false);
        this.fontSize = (Value<Integer>)this.addSetting(new Value("FontSize", 21, "Size of the font."));
    }

}
