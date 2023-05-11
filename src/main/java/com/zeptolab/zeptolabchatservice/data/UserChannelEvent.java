package com.zeptolab.zeptolabchatservice.data;

import jakarta.validation.constraints.NotBlank;

public record UserChannelEvent(@NotBlank String channel) {
}
