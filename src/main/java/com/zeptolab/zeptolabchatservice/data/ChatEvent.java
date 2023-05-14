package com.zeptolab.zeptolabchatservice.data;

import jakarta.validation.constraints.NotBlank;

public record ChatEvent(@NotBlank String text,
                        @NotBlank String channel,
                        @NotBlank String username) {

}
