

package com.ragemachine.moon.impl.client;

import com.ragemachine.moon.MoonHack;
import com.ragemachine.moon.impl.client.gui.MoonHackGui;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import com.ragemachine.moon.impl.manager.TextManager;
import com.ragemachine.moon.impl.util.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Hack
		implements Util {
	public List<Value> values = new ArrayList<Value>();
	public TextManager renderer = MoonHack.TEXT_MANAGER;
	private String name;

	public Hack() {
	}

	public Hack(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public List<Value> getSettings() {
		return this.values;
	}

	public boolean hasSettings() {
		if (this.values.isEmpty()) return false;
		return true;
	}

	public boolean isEnabled() {
		if (!(this instanceof Module)) return false;
		return ((Module)this).isOn();
	}

	public boolean isDisabled() {
		if (this.isEnabled()) return false;
		return true;
	}

	public Value addSetting(Value value) {
		value.setFeature(this);
		this.values.add(value);
		if (!(this instanceof Module)) return value;
		if (!(Hack.mc.currentScreen instanceof MoonHackGui)) return value;
		MoonHackGui.getInstance().updateModule((Module)this);
		return value;
	}

	public void unaddSetting(Value valueIn) {
		ArrayList<Value> removeList = new ArrayList<Value>();
		for (Value value : this.values) {
			if (!value.equals(valueIn)) continue;
			removeList.add(value);
		}
		if (!(this instanceof Module)) return;
		if (!(Hack.mc.currentScreen instanceof MoonHackGui)) return;
		if (!removeList.isEmpty()) {
			this.values.removeAll(removeList);
		}
		MoonHackGui.getInstance().updateModule((Module)this);
	}


	public static boolean fullNullCheck() {
		if (Hack.mc.player == null) return true;
		if (Hack.mc.world == null) return true;
		return false;
	}


	public Value getSettingByName(String name) {
		Value value;
		Iterator<Value> iterator = this.values.iterator();
		do {
			if (!iterator.hasNext()) return null;
		} while (!(value = iterator.next()).getName().equalsIgnoreCase(name));
		return value;
	}

	public void reset() {
		Iterator<Value> iterator = this.values.iterator();
		while (iterator.hasNext()) {
			Value value = iterator.next();
			value.setValue(value.getDefaultValue());
		}
	}

	public void removeSetting(Value valueIn) {
		ArrayList<Value> removeList = new ArrayList<Value>();
		for (Value value : this.values) {
			if (!value.equals(valueIn)) continue;
			removeList.add(value);
		}
		if (!(this instanceof Module)) return;
		if (!(Hack.mc.currentScreen instanceof MoonHackGui)) return;
		if (!removeList.isEmpty()) {
			this.values.removeAll(removeList);
		}
		MoonHackGui.getInstance().updateModule((Module)this);
	}

	public void clearSettings() {
		this.values = new ArrayList<Value>();
	}

	public static boolean nullCheck() {
		if (Hack.mc.player != null) return false;
		return true;
	}
}

