package com.andreidodu.aresguard.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ActionEnum {
    WRITE_FILE("-w"),
    UNDEFINED("");

    private final String val;

    ActionEnum(String val) {
        this.val = val;
    }

    private static final Map<String, ActionEnum> lookup = new HashMap<>();

    static {
        for (final ActionEnum s : EnumSet.allOf(ActionEnum.class)) {
            lookup.put(s.getVal(), s);
        }
    }

    static public ActionEnum fromString(final String name) {
        return lookup.get(name);
    }

    private String getVal() {
        return this.val;
    }

}
