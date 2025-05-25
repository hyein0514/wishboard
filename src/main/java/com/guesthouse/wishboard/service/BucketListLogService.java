package com.guesthouse.wishboard.service;

import com.guesthouse.wishboard.dto.BucketListLogResponseDto;
import com.guesthouse.wishboard.entity.BucketList_log;
import com.guesthouse.wishboard.repository.BucketListLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
@RequiredArgsConstructor
public class BucketListLogService {

    private final BucketListLogRepository logRepository;

    public BucketListLogResponseDto getPrevLog(Long logId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        BucketList_log current = logRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("기록이 존재하지 않습니다."));

        if (!current.getBucketList().getUser().getUserId().equals(userId)) {
            throw new RuntimeException("접근 권한이 없습니다.");
        }

        BucketList_log prev = logRepository.findTopByBucketIdAndCreatedAtLessThanOrderByCreatedAtDesc(
                current.getBucketId(), current.getCreatedAt());

        if (prev == null) {
            throw new RuntimeException("이전 기록이 없습니다.");
        }

        return BucketListLogResponseDto.builder()
                .logId(prev.getLogId())
                .createdAt(toDateStr(prev.getCreatedAt()))
                .image(prev.getImage())
                .content(prev.getContent())
                .build();
    }

    public BucketListLogResponseDto getNextLog(Long logId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        BucketList_log current = logRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("기록이 존재하지 않습니다."));

        if (!current.getBucketList().getUser().getUserId().equals(userId)) {
            throw new RuntimeException("접근 권한이 없습니다.");
        }

        BucketList_log next = logRepository.findTopByBucketIdAndCreatedAtGreaterThanOrderByCreatedAtAsc(
                current.getBucketId(), current.getCreatedAt());

        if (next == null) {
            throw new RuntimeException("다음 기록이 없습니다.");
        }

        return BucketListLogResponseDto.builder()
                .logId(next.getLogId())
                .createdAt(toDateStr(next.getCreatedAt()))
                .image(next.getImage())
                .content(next.getContent())
                .build();
    }

    private String toDateStr(java.util.Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
