package com.zeptolab.zeptolabchatservice.data;

import jakarta.validation.constraints.NotBlank;


public record JoinEvent(@NotBlank String channel) {

}
