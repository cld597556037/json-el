package com.dong.json.el;

public enum Comparator {
    LT("<", "小于"),
    GT(">", "大于"),
    LE("<=", "小于等于"),
    GE(">=", "大于等于"),
    BT("~", "范围"),
    IN("in", "被包含");


    String name;
    String desc;

    Comparator(String name, String desc){
        this.name = name;
        this.desc = desc;
    }

    static Boolean is(String value) {
        for (Comparator comparator : Comparator.values()) {
            if (comparator.name.equals(value)) {
                return true;
            }
        }
        return false;
    }

    static Comparator get(String value) {
        for (Comparator comparator : Comparator.values()) {
            if (comparator.name.equals(value)) {
                return comparator;
            }
        }
        return null;
    }
}
