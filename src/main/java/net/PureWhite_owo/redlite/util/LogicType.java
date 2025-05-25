package net.PureWhite_owo.redlite.util;

public enum LogicType {
    AND,OR,NOT;

    public LogicType next() {
        return switch (this) {
            case AND -> OR;
            case OR -> NOT;
            case NOT -> AND;
        };
    }
}
