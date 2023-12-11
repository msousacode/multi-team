package com.multiteam.modules.sign.payload;

import java.util.List;

public record TokenDTO(String userId, String userName, List<String> roles) {
}
