package com.guesthouse.wishboard.dto;

import java.util.List;
import lombok.Data;

@Data
public class AIRequestDto {
    // 완료한 버킷리스트 제목들
    private List<String> completedGoals;
}
