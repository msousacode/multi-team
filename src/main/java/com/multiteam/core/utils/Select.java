package com.multiteam.core.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class Select {

    private String name;
    private String code;

    public Select(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public static Select toSelect(String name, String code) {
        return new Select(name, code);
    }
}
