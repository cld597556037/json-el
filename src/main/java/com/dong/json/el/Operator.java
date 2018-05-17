package com.dong.json.el;

public enum Operator {
    AND("&", "与"),
    OR("|", "或"),
    NOT("!", "非");


    String name;
    String desc;

    Operator(String name, String desc){
        this.name = name;
        this.desc = desc;
    }

    static Boolean is(String value) {
        for (Operator operator : Operator.values()) {
            if (operator.name.equals(value)) {
                return true;
            }
        }
        return false;
    }

    static Operator get(String value) {
        for (Operator operator : Operator.values()) {
            if (operator.name.equals(value)) {
                return operator;
            }
        }
        return null;
    }

}
