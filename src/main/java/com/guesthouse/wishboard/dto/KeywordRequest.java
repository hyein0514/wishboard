package com.guesthouse.wishboard.dto;

import jakarta.validation.constraints.NotBlank;

public record KeywordRequest(
        @NotBlank String keyword,
        Boolean notification
) { }
