package com.zeptolab.zeptolabchatservice.data;

import jakarta.validation.constraints.NotBlank;

public record LoginData(@NotBlank String name, @NotBlank String password) {
}
