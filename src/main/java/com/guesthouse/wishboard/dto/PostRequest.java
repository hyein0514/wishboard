package com.guesthouse.wishboard.dto;

public record PostRequest(
        String type,               // 정보, Q&A, 트로피, 인원모집
        String communityType,      // 익스트림 스포츠
        String diversity,          // 수상스키
        String title,
        String content,
        Long   bucketId            // 연결할 버킷 ID
) { }
