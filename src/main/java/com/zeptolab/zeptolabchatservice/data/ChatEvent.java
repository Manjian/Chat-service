package com.zeptolab.zeptolabchatservice.data;

import com.zeptolab.zeptolabchatservice.repositories.type.MessageType;
import jakarta.validation.constraints.NotBlank;

public record ChatEvent(@NotBlank MessageType messageType,
                        @NotBlank String text,
                        @NotBlank String channel,
                        @NotBlank String username) {

}
