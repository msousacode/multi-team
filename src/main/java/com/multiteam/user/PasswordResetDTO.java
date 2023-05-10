package com.multiteam.user;

import java.util.UUID;

public record PasswordResetDTO(UUID id, String password1, String password2){}
