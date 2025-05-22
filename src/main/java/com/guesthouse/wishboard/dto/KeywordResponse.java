package com.guesthouse.wishboard.dto;
import com.guesthouse.wishboard.entity.Keyword;


public record KeywordResponse(
        Long keywordId,
        String keyword,
        boolean notification
) {
    public static KeywordResponse from(Keyword k) {
        return new KeywordResponse(
                k.getKeywordId(),
                k.getKeyword(),
                k.isNotification()
        );
    }
}

