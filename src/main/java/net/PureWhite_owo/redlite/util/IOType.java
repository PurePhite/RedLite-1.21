package net.PureWhite_owo.redlite.util;

public enum IOType {
    NONE,INPUT,OUTPUT;

    public IOType next() {
        return switch (this) {
            case INPUT -> OUTPUT;
            case OUTPUT -> NONE;
            case NONE -> INPUT;
        };
    }
}

