package com.guesthouse.wishboard.service;

import com.guesthouse.wishboard.dto.BucketListLogRequestDto;
import com.guesthouse.wishboard.dto.BucketListLogResponseDto;
import com.guesthouse.wishboard.entity.BucketList;
import com.guesthouse.wishboard.entity.BucketList_log;
import com.guesthouse.wishboard.repository.BucketListLogRepository;
import com.guesthouse.wishboard.repository.BucketListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BucketListLogService {

    private final BucketListLogRepository logRepository;
    private final BucketListRepository bucketListRepository;

    public void createLog(BucketListLogRequestDto dto) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        BucketList bucketList = bucketListRepository.findByBucketIdAndUser_UserId(dto.getBucketId(), userId)
                .orElseThrow(() -> new RuntimeException("버킷리스트가 존재하지 않습니다."));

        BucketList_log log = BucketList_log.builder()
                .createdAt(parseDate(dto.getCreatedAt()))
                .image(dto.getImage())
                .content(dto.getContent())
                .bucketId(dto.getBucketId())
                .bucketList(bucketList)
                .build();

        logRepository.save(log);
    }

    public List<BucketListLogResponseDto> getLogList(Long bucketId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        BucketList bucketList = bucketListRepository.findByBucketIdAndUser_UserId(bucketId, userId)
                .orElseThrow(() -> new RuntimeException("버킷리스트가 존재하지 않거나 접근 권한이 없습니다."));

        return logRepository.findByBucketIdOrderByCreatedAtAsc(bucketId).stream()
                .map(log -> BucketListLogResponseDto.builder()
                        .logId(log.getLogId())
                        .createdAt(toDateStr(log.getCreatedAt()))
                        .image(log.getImage())
                        .content(log.getContent())
                        .build())
                .toList();
    }

    public BucketListLogResponseDto getLogDetail(Long logId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        BucketList_log log = logRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("도전 기록이 존재하지 않습니다."));

        if (!log.getBucketList().getUser().getUserId().equals(userId)) {
            throw new RuntimeException("해당 기록에 접근할 권한이 없습니다.");
        }

        return BucketListLogResponseDto.builder()
                .logId(log.getLogId())
                .createdAt(toDateStr(log.getCreatedAt()))
                .image(log.getImage())
                .content(log.getContent())
                .build();
    }

    public void deleteLog(Long logId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        BucketList_log log = logRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("도전 기록이 존재하지 않습니다."));

        if (!log.getBucketList().getUser().getUserId().equals(userId)) {
            throw new RuntimeException("해당 기록을 삭제할 권한이 없습니다.");
        }

        logRepository.delete(log);
    }

    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            throw new RuntimeException("날짜 형식이 잘못되었습니다 (yyyy-MM-dd).");
        }
    }

    private String toDateStr(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
