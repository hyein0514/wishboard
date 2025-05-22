package com.guesthouse.wishboard.service;

import com.guesthouse.wishboard.dto.TrophyDto;
import com.guesthouse.wishboard.dto.TrophyDetail1Dto;
import com.guesthouse.wishboard.dto.TrophyDetail2Dto;
import com.guesthouse.wishboard.entity.BucketList;
import com.guesthouse.wishboard.entity.BucketList_log;
import com.guesthouse.wishboard.entity.User;
import com.guesthouse.wishboard.jwt.CustomUserDetail;
import com.guesthouse.wishboard.repository.TrophyBucketListLogRepository;
import com.guesthouse.wishboard.repository.TrophyRepository;
import com.guesthouse.wishboard.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrophyService {
    private final TrophyRepository trophyRepository;
    private final TrophyBucketListLogRepository trophyBucketListLogRepository;
    private final UserRepository userRepository;

    // 사용자의 완료된 버킷리스트 조회
    public List<TrophyDto> getUserTrophies(@AuthenticationPrincipal CustomUserDetail users) {
        User user = userRepository.findAllByUserId(users.getUsername());
        System.out.println("💡 로그인된 유저의 DB ID: " + user.getId());    
        List<BucketList> bucketLists = trophyRepository.findByUser_IdAndStatus(user.getId(), "완료");
            return bucketLists.stream()
                    .map(bucket -> new TrophyDto(
                            bucket.getBucketId(),
                            bucket.getTitle(),
                            bucket.getCategory(),
                            bucket.getAchievedAt(),
                            bucket.getStatus(),
                            bucket.getCreatedAt(),
                            bucket.getUserId(),
                            bucket.getTrophy()
                    ))
                    .collect(Collectors.toList());
        }
    

    // 버킷리스트트 ID에 해당하는 trophyDetail1 조회
    public TrophyDetail1Dto getBucketDetail(Long bucketId, @AuthenticationPrincipal CustomUserDetail users) {
        User user = userRepository.findAllByUserId(users.getUsername());
        BucketList bucket = trophyRepository.findById(bucketId)
                .orElseThrow(() -> new IllegalArgumentException("해당 버킷리스트가 존재하지 않습니다."));
        
        if (!bucket.getUser().equals(user)) {
            throw new SecurityException("해당 버킷리스트에 접근할 수 없습니다.");
        }
        
        return new TrophyDetail1Dto(
                bucket.getBucketId(),
                bucket.getTitle(),
                bucket.getCategory(),
                bucket.getAchievedAt(),
                bucket.getCreatedAt(),
                bucket.getReason(),
                bucket.getResolution(),
                bucket.getImage(),
                bucket.getUserId()
        );
    }

    // 버킷리스트트 ID에 해당하는 trophyDetail2(log들) 조회
     public List<TrophyDetail2Dto> getBucketLogsSorted(Long bucketId, @AuthenticationPrincipal CustomUserDetail users) {
        User user = userRepository.findAllByUserId(users.getUsername());
        BucketList bucket = trophyRepository.findById(bucketId)
                .orElseThrow(() -> new IllegalArgumentException("해당 버킷리스트가 없습니다."));
        
        if (!bucket.getUser().equals(user)) {
                throw new SecurityException("해당 버킷리스트에 접근할 수 없습니다.");
            }

        
        List<BucketList_log> logs = trophyBucketListLogRepository.findByBucketIdOrderByCreatedAtAsc(bucketId);

        return logs.stream().map(log -> new TrophyDetail2Dto(
                bucket.getBucketId(),
                bucket.getTitle(),
                bucket.getCategory(),
                bucket.getAchievedAt(),
                bucket.getCreatedAt(),
                log.getLogId(),
                log.getCreatedAt(),
                log.getImage(),
                log.getContent()
        )).collect(Collectors.toList());
    }
    
    // 버킷리스트 ID에 해당하는 trophyDetail1 조회(명예의전당)
    public TrophyDetail1Dto getBucketDetailH(Long bucketId) {
        BucketList bucket = trophyRepository.findById(bucketId)
                .orElseThrow(() -> new IllegalArgumentException("해당 버킷리스트가 존재하지 않습니다."));
        
        return new TrophyDetail1Dto(
                bucket.getBucketId(),
                bucket.getTitle(),
                bucket.getCategory(),
                bucket.getAchievedAt(),
                bucket.getCreatedAt(),
                bucket.getReason(),
                bucket.getResolution(),
                bucket.getImage(),
                bucket.getUserId()
        );
    }

    // 버킷리스트 ID에 해당하는 trophyDetail2(log들) 조회(명예의전당)
     public List<TrophyDetail2Dto> getBucketLogsSortedH(Long bucketId) {
        BucketList bucket = trophyRepository.findById(bucketId)
                .orElseThrow(() -> new IllegalArgumentException("해당 버킷리스트가 없습니다."));

        
        List<BucketList_log> logs = trophyBucketListLogRepository.findByBucketIdOrderByCreatedAtAsc(bucketId);

        return logs.stream().map(log -> new TrophyDetail2Dto(
                bucket.getBucketId(),
                bucket.getTitle(),
                bucket.getCategory(),
                bucket.getAchievedAt(),
                bucket.getCreatedAt(),
                log.getLogId(),
                log.getCreatedAt(),
                log.getImage(),
                log.getContent()
        )).collect(Collectors.toList());
    }
}
