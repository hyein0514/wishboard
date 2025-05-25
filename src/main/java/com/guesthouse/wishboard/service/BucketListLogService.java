package com.guesthouse.wishboard.service;

import com.guesthouse.wishboard.dto.BucketListLogRequestDto;
import com.guesthouse.wishboard.entity.BucketList;
import com.guesthouse.wishboard.entity.BucketList_log;
import com.guesthouse.wishboard.repository.BucketListLogRepository;
import com.guesthouse.wishboard.repository.BucketListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class BucketListLogService {

    private final BucketListLogRepository logRepository;
    private final BucketListRepository bucketListRepository;

    public void createLog(BucketListLogRequestDto dto) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 인증된 사용자의 버킷리스트인지 확인
        BucketList bucketList = bucketListRepository.findByBucketIdAndUser_UserId(dto.getBucketId(), userId)
                .orElseThrow(() -> new RuntimeException("해당 버킷리스트를 찾을 수 없습니다."));

        BucketList_log log = BucketList_log.builder()
                .createdAt(parseDate(dto.getCreatedAt()))
                .image(dto.getImage())
                .content(dto.getContent())
                .bucketId(dto.getBucketId()) // 필드에도 설정
                .bucketList(bucketList)      // 연관관계도 설정
                .build();

        logRepository.save(log);
    }

    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            throw new RuntimeException("날짜 형식이 잘못되었습니다 (yyyy-MM-dd).");
        }
    }
}
