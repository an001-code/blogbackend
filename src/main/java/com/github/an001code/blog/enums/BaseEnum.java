package com.github.an001code.blog.enums;

public interface BaseEnum {
    int getValue();
    String getDesc();

    default boolean equalsValue(Integer value){
        if (value == null) {
            return false;
        }
        return getValue() == value;
    }
}