package com.multiteam.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerate {

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("12345678"));
    }
}
