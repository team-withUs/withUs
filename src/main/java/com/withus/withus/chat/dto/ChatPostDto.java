package com.withus.withus.chat.dto;

import jakarta.validation.constraints.NotNull;

public record ChatPostDto(
    @NotNull
    Long id

) {


}
