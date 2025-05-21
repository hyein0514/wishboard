// src/main/java/com/guesthouse/wishboard/service/CommunityService.java
package com.guesthouse.wishboard.service;

import com.guesthouse.wishboard.dto.CommunitySearchResponse;
import com.guesthouse.wishboard.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 게시글 목록 / 상세 비즈니스 로직
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityService {

    private final CommunityRepository communityRepository;

    // 검색 기능
    public List<CommunitySearchResponse> searchBySubKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("keyword 파라미터가 비어 있습니다.");
        }
        return communityRepository.findCommunitiesBySubKeyword(keyword.trim());
    }

}
