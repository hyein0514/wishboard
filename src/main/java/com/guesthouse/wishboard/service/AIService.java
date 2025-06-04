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
            너는 사용자가 완료한 목표들을 기반으로, 다음 단계의 목표를 추천하는 전문가야.

            1. 역할:
            - 사용자가 완료한 각 목표당 3개의 다음 목표를 추천해줘.
            - 사용자가 달성한 버킷리스트를 분석해, 그와 연관되거나 발전된 목표를 핵심 **명사 형태의 키워드**로 추천해줘.
            - 추천 키워드는 **짧고 강렬한 명사 또는 명사구**로 구성되어야 해.

            2. 금지사항:
            - **절대 동사, 조사, 형용사, 설명어(예: 체험, 도전, 참가, 여행, 등반 등)**를 포함하지 마.
            - 예: `한라산 등반` → `한라산` / `열기구 타기` → `열기구`
            - 한 단어로 충분히 의미 전달이 되도록 해야 해.
            - 아래와 같은 단어가 포함된 키워드는 무조건 제거해:  `체험`, `도전`, `도전하기`, `참가`, `참여`, `해보기`, `가기`, `타기`, `올라가기`, `등반`, `방문`, `여행`

            3. 출력 형식:
            - 출력 앞에 `추천:`, `1.`, `-` 등의 접두어를 붙이지 마.
            - 오직 **쉼표(,)로 구분된 1줄 키워드 목록**만 출력 (줄바꿈 없이)
            - 키워드는 **띄어쓰기 없이** 출력 (예: `북극탐험`)
            - 예시 출력: `스카이다이빙,한라산,열기구,노지캠핑,로드트립,사하라`

            4. 조건:
            - 총 추천 키워드 수는 사용자가 완료한 목표 개수 × 3개
            - 예: 완료 목표가 4개면 **12개 키워드를 출력**
            - 각 완료 목표에 대해 **추천 키워드 3개씩**
            - 중복되거나 유사한 키워드는 제외하고 **다양한 분야의 키워드**로 구성할 것

            5. 사용자가 완료한 목표들:
            """ + String.join(", ", completedGoals) + """

            위 목록을 참고하여 조건에 맞게 다음 목표를 추천해줘.
            """;


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
                            (c.getBucketId() != null ? c.getBucketId() : 0L),
                            (c.getCommunityDiversity() != null ? c.getCommunityDiversity() : "")
                    ));
                }
            } else {
                // 매칭 게시글이 없을 경우 → AI 추천 태그로 대체
                result.add(new AIRecommendItemDto("AI 추천", keyword, "AI", 0, 0, 0L, 0L,""));
            }
        }
        // 결과 포장 후 반환
        return new AIResponseDto(result);
    }
}
