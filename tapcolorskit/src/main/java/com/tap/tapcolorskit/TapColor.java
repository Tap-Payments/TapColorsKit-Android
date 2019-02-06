package com.tap.tapcolorskit;

public class TapColor {
    private final String    name;
    private final String    code;

    public TapColor(final String name,final String code){
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof TapColor && this.code == ((TapColor)o).code ;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
