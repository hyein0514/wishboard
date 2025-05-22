package com.guesthouse.wishboard.service;

import com.guesthouse.wishboard.dto.KeywordRequest;
import com.guesthouse.wishboard.dto.KeywordResponse;
import com.guesthouse.wishboard.entity.Keyword;
import com.guesthouse.wishboard.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordService {

    private final KeywordRepository keywordRepo;

    /* 키워드 등록 */
    @Transactional
    public KeywordResponse add(KeywordRequest req, Long userId) {

        if (keywordRepo.existsByUserIdAndKeyword(userId, req.keyword()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 등록된 키워드");

        Keyword saved = keywordRepo.save(
                Keyword.builder()
                        .keyword(req.keyword())
                        .notification(req.notification() == null ? true : req.notification())  // ← Boolean → boolean
                        .userId(userId)
                        .build());

        return KeywordResponse.from(saved);
    }

    /* 키워드 삭제 */
    @Transactional
    public void delete(Long keywordId, Long userId) {
        Keyword k = keywordRepo.findById(keywordId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "keyword"));

        if (!k.getUserId().equals(userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "not owner");

        keywordRepo.delete(k);
    }

    /* 키워드 목록 */
    public List<KeywordResponse> list(Long userId) {
        return keywordRepo.findByUserIdOrderByKeywordIdDesc(userId)
                .stream()
                .map(KeywordResponse::from)
                .toList();
    }
}
