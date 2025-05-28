package com.guesthouse.wishboard.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class AIResponseDto {
    private List<AIRecommendItemDto> recommendations;
}
