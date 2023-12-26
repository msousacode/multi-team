package com.multiteam.core.utils;

import org.jetbrains.annotations.Contract;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtil {

    @Contract("_ -> fail")//Indica que o método nunca deve retornar um valor nulo.
    public static Object getPrincipalAuthenticaded() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal();
    }
}
