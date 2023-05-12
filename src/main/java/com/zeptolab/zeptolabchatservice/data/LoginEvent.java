package com.zeptolab.zeptolabchatservice.data;

import jakarta.validation.constraints.NotBlank;

public record LoginEvent(@NotBlank String name,
                         @NotBlank String password) {
}
