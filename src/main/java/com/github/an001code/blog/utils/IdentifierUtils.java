package com.github.an001code.blog.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IdentifierUtils {

    public static String generateIdentifier(Long id){
        Long number = id + 1000000000L;
        String identifier = String.valueOf(number);
        return identifier;
    }
}
