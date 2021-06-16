
package com.ragemachine.moon.impl.client.setting;

import com.ragemachine.moon.impl.client.Hack;
import com.ragemachine.moon.api.event.events.ClientEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.function.Predicate;

public class Value<valueType> {
    private final String name;
    private final valueType defaultValue;
    private valueType value;
    private valueType plannedValue;
    private boolean button;
    private valueType min;
    private valueType max;
    private boolean hasRestriction;
    private boolean shouldRenderStringName;
    private Predicate<valueType> visibility;
    private String description;
    private Hack hack;
    private String current;
    private double slider;

    public Value(String name, valueType defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.plannedValue = defaultValue;
        this.description = "";
    }

    public Value(String name, valueType defaultValue, String description) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.plannedValue = defaultValue;
        this.description = description;
    }

    public Value(String name, valueType defaultValue, valueType min, valueType max, String description) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.plannedValue = defaultValue;
        this.description = description;
        this.hasRestriction = true;
    }

    public Value(String name, valueType defaultValue, valueType min, valueType max) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.plannedValue = defaultValue;
        this.description = "";
        this.hasRestriction = true;
    }

    public Value(String name, valueType defaultValue, valueType min, valueType max, Predicate<valueType> visibility, String description) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.plannedValue = defaultValue;
        this.visibility = visibility;
        this.description = description;
        this.hasRestriction = true;
    }

    public Value(String name, valueType defaultValue, valueType min, valueType max, Predicate<valueType> visibility) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.plannedValue = defaultValue;
        this.visibility = visibility;
        this.description = "";
        this.hasRestriction = true;
    }

    public Value(String name, valueType defaultValue, Predicate<valueType> visibility) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.visibility = visibility;
        this.plannedValue = defaultValue;
    }

    public String getName() {
        return this.name;
    }

    public valueType getValue() {
        return this.value;
    }

    public int getIntVal(int type) {
        return ((int) Math.round(this.slider));
    }

    public valueType getPlannedValue() {
        return this.plannedValue;
    }

    public void setPlannedValue(valueType value) {
        this.plannedValue = value;
    }

    public valueType getMin() {
        return this.min;
    }

    public valueType getMax() {
        return this.max;
    }

    public void setValue(valueType value) {
        this.setPlannedValue(value);
        if (this.hasRestriction) {
            if (((Number)this.min).floatValue() > ((Number)value).floatValue()) {
                this.setPlannedValue(this.min);
            }
            if (((Number)this.max).floatValue() < ((Number)value).floatValue()) {
                this.setPlannedValue(this.max);
            }
        }
        ClientEvent event = new ClientEvent(this);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            this.value = this.plannedValue;
            return;
        }
        this.plannedValue = this.value;
    }

    public void setValueNoEvent(valueType value) {
        this.setPlannedValue(value);
        if (this.hasRestriction) {
            if (((Number)this.min).floatValue() > ((Number)value).floatValue()) {
                this.setPlannedValue(this.min);
            }
            if (((Number)this.max).floatValue() < ((Number)value).floatValue()) {
                this.setPlannedValue(this.max);
            }
        }
        this.value = this.plannedValue;
    }

    public void setMin(valueType min) {
        this.min = min;
    }

    public void setMax(valueType max) {
        this.max = max;
    }

    public void setFeature(Hack hack) {
        this.hack = hack;
    }

    public Hack getFeature() {
        return this.hack;
    }

    public int getEnum(String input) {
        int i = 0;
        while (i < this.value.getClass().getEnumConstants().length) {
            Enum e = (Enum)this.value.getClass().getEnumConstants()[i];
            if (e.name().equalsIgnoreCase(input)) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    public void setEnumValue(String value) {
        Enum[] arrenum = (Enum[])((Enum)this.value).getClass().getEnumConstants();
        int n = arrenum.length;
        int n2 = 0;
        while (n2 < n) {
            Enum e = arrenum[n2];
            if (e.name().equalsIgnoreCase(value)) {
                this.value = (valueType) e;
            }
            ++n2;
        }
    }

    public boolean in(String value) {
        return this.current.equalsIgnoreCase(value);
    }

    public String currentEnumName() {
        return EnumConverter.getProperName((Enum)this.value);
    }

    public int currentEnum() {
        return EnumConverter.currentEnum((Enum)this.value);
    }

    public void increaseEnum() {
        this.plannedValue = (valueType) EnumConverter.increaseEnum((Enum)this.value);
        ClientEvent event = new ClientEvent(this);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            this.value = this.plannedValue;
            return;
        }
        this.plannedValue = this.value;
    }

    public void increaseEnumNoEvent() {
        this.value = (valueType) EnumConverter.increaseEnum((Enum)this.value);
    }

    public String getType() {
        if (!this.isEnumSetting()) return this.getClassName(this.defaultValue);
        return "Enum";
    }

    public <T> String getClassName(T value) {
        return value.getClass().getSimpleName();
    }

    public String getDescription() {
        if (this.description != null) return this.description;
        return "";
    }

    public boolean isNumberSetting() {
        if (this.value instanceof Double) return true;
        if (this.value instanceof Integer) return true;
        if (this.value instanceof Short) return true;
        if (this.value instanceof Long) return true;
        if (this.value instanceof Float) return true;
        return false;
    }

    public boolean isEnumSetting() {
        if (this.isNumberSetting()) return false;
        if (this.value instanceof String) return false;
        if (this.value instanceof Bind) return false;
        if (this.value instanceof Character) return false;
        if (this.value instanceof Boolean) return false;
        return true;
    }

    public boolean isStringSetting() {
        return this.value instanceof String;
    }

    public valueType getDefaultValue() {
        return this.defaultValue;
    }

    public String getValueAsString() {
        return this.value.toString();
    }

    public boolean hasRestriction() {
        return this.hasRestriction;
    }

    public void setVisibility(Predicate<valueType> visibility) {
        this.visibility = visibility;
    }

    public Value<valueType> setRenderName(boolean renderName) {
        this.shouldRenderStringName = renderName;
        return this;
    }

    public boolean shouldRenderName() {
        if (this.isStringSetting()) return this.shouldRenderStringName;
        return true;
    }

    public boolean isVisible() {
        if (this.visibility != null) return this.visibility.test(this.getValue());
        return true;
    }

    public boolean getMoonButtonVal(boolean type) {
        return this.button;
    }
}

