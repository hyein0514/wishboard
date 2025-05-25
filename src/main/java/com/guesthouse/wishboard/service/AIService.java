package com.guesthouse.wishboard.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.guesthouse.wishboard.dto.AIRecommendItemDto;
import com.guesthouse.wishboard.dto.AIResponseDto;
import com.guesthouse.wishboard.entity.Community;
import com.guesthouse.wishboard.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AIService {
    @Value("${openai.api-key}") // OpenAI API Key를 application.yml에서 받아옴.
    private String apiKey;

    private final CommunityRepository communityRepository;

    private WebClient webClient;

    // Bean 초기화 후 WebClient 설정
    @PostConstruct
    public void init() {
        webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    //사용자가 완료한 목표 목록을 기반으로 GPT에게 다음 목표 키워드 추천 요청
    public List<String> getRecommendedKeywordsFromGPT(List<String> completedGoals) {
        // GPT에게 전달할 프롬프트 정의
        String prompt = """
            너는 사용자가 완료한 목표들을 기반으로 다음 단계의 목표를 추천해주는 전문가야.

<<<<<<< HEAD
조건:
- 각 추천은 **단어 또는 짧은 명사구**로만 구성되어야 해.  
- **동사를 포함하지 말고**, 핵심 키워드만 사용해.  
  - 예시: ‘스카이다이빙’ → O, ‘스카이다이빙 체험’ → X  
  - ‘열기구 타기’ → X, ‘열기구’ → O  
  - ‘철인 3종 경기 참가’ → X, ‘철인 3종 경기’ → O
- **형식은 반드시 쉼표로만 구분된 키워드 1줄 출력** (줄바꿈 없이)
- 각 완료된 목표에 대해 3개씩 추천하되, 총 추천 수는 5개로 제한해.
- **첫 번째 추천 키워드는 반드시 '한라산'이어야 하며, 맨 앞에 위치해야 해.**
- 출력 예시: `한라산, 스카이다이빙, 열기구, 철인 3종 경기, 북극 탐험`

아래는 사용자가 완료한 목표들이야:

""" + String.join(", ", completedGoals) + """

위 목표를 기반으로 다음 목표들을 추천해줘.
""";
=======
            조건:
            - 각 추천은 **단어 또는 짧은 명사구**로만 구성되어야 해.  
            - **동사를 포함하지 말고**, 핵심 키워드만 사용해.  
            - 예시: ‘스카이다이빙’ → O, ‘스카이다이빙 체험’ → X  
            - ‘열기구 타기’ → X, ‘열기구’ → O  
            - ‘철인 3종 경기 참가’ → X, ‘철인 3종 경기’ → O
            - **형식은 반드시 쉼표로만 구분된 키워드 1줄 출력** (줄바꿈 없이)
            - 각 완료된 목표에 대해 3개씩 추천하되, 총 추천 수는 5개로 제한해.
            - **첫 번째 추천 키워드는 반드시 '스카이다이빙'이어야 하며, 맨 앞에 위치해야 해.**
            - 출력 예시: `한라산, 스카이다이빙, 열기구, 철인 3종 경기, 북극 탐험`

            아래는 사용자가 완료한 목표들이야:

            """ + String.join(", ", completedGoals) + """

            위 목표를 기반으로 다음 목표들을 추천해줘.
            """;
>>>>>>> 8702cb5 (feat: AI 추천 기능 구현현)

            
        // OpenAI chat completion API 요청 형식
        Map<String, Object> request = Map.of(
                "model", "gpt-4",
                "messages", List.of(Map.of("role", "user", "content", prompt))
        );

        // WebClient를 사용해 GPT 응답 요청 및 파싱
        String result = webClient.post()
                .uri("/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(res -> res.get("choices").get(0).get("message").get("content").asText())
                .block();

        // 리스트 변환
        return Arrays.stream(result.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    // GPT 추천 키워드를 기반으로 DB에서 관련 커뮤니티 항목 조회 후 DTO로 반환
    public AIResponseDto getFinalRecommendations(List<String> completedGoals) {
        List<String> keywords = getRecommendedKeywordsFromGPT(completedGoals);

        List<AIRecommendItemDto> result = new ArrayList<>();

        for (String keyword : keywords) {
            // 키워드가 포함된 게시글 검색
            List<Community> matches = communityRepository.findByTitleContainingIgnoreCase(keyword);
            if (!matches.isEmpty()) {
                for (Community c : matches) {
                    // 버킷리스트 카테고리 추출
                    String category = (c.getBucketList() != null && c.getBucketList().getCategory() != null)
                            ? c.getBucketList().getCategory() : "";

                    // 추천 항목 DTO 생성 및 추가
                    result.add(new AIRecommendItemDto(
                            category,
                            c.getTitle(),
                            (c.getUser() != null ? c.getUser().getNickname() : "익명"),
                            (c.getLikes() != null ? c.getLikes().size() : 0),
                            (c.getComments() != null ? c.getComments().size() : 0),
                            (c.getCommunityId() != null ? c.getCommunityId() : 0L),
                            (c.getBucketId() != null ? c.getBucketId() : 0L)
                    ));
                }
            } else {
                // 매칭 게시글이 없을 경우 → AI 추천 태그로 대체
                result.add(new AIRecommendItemDto("AI 추천", keyword, "AI", 0, 0, 0L, 0L));
            }
        }
        // 결과 포장 후 반환
        return new AIResponseDto(result);
    }
}