package com.guesthouse.wishboard.service;

import com.guesthouse.wishboard.dto.BucketListRequestDto;
import com.guesthouse.wishboard.dto.BucketListResponseDto;
import com.guesthouse.wishboard.dto.BucketListDetailDto;
import com.guesthouse.wishboard.dto.BucketListHomeDto;
import com.guesthouse.wishboard.entity.BucketList;
import com.guesthouse.wishboard.entity.User;
import com.guesthouse.wishboard.repository.BucketListRepository;
import com.guesthouse.wishboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BucketListService {

    private final BucketListRepository bucketListRepository;
    private final UserRepository userRepository;

    public void createBucketList(BucketListRequestDto dto) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserId(userId).orElseThrow();

        BucketList bucketList = new BucketList();
        bucketList.setTitle(dto.getTitle());
        bucketList.setCategory(dto.getCategory());
        bucketList.setTargetDate(parseDate(dto.getTargetDate()));
        bucketList.setImage(dto.getImage());
        bucketList.setReason(dto.getReason());
        bucketList.setResolution(dto.getResolution());
        bucketList.setPinToTop(false);
        bucketList.setStatus("ongoing");
        bucketList.setTrophy("false");
        bucketList.setCreatedAt(new Date());
        bucketList.setUser(user);

        bucketListRepository.save(bucketList);
    }

    public List<BucketListResponseDto> getMyBucketLists() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<BucketList> list = bucketListRepository.findByUser_UserIdOrderByPinToTopDescTargetDateAsc(userId);

        return list.stream().map(b -> BucketListResponseDto.builder()
                .bucketId(b.getBucketId())
                .title(b.getTitle())
                .category(b.getCategory())
                .targetDate(toDateStr(b.getTargetDate()))
                .image(b.getImage())
                .pinToTop(b.getPinToTop())
                .dDay(calcDDay(b.getTargetDate()))
                .build()
        ).toList();
    }

    public BucketListDetailDto getBucketListDetail(Long id) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        BucketList b = bucketListRepository.findByBucketIdAndUser_UserId(id, userId)
                .orElseThrow(() -> new RuntimeException("버킷리스트가 존재하지 않습니다."));

        return BucketListDetailDto.builder()
                .bucketId(b.getBucketId())
                .title(b.getTitle())
                .category(b.getCategory())
                .targetDate(toDateStr(b.getTargetDate()))
                .image(b.getImage())
                .reason(b.getReason())
                .resolution(b.getResolution())
                .pinToTop(b.getPinToTop())
                .status(b.getStatus())
                .build();
    }

    public void deleteBucketList(Long id) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        BucketList bucketList = bucketListRepository.findByBucketIdAndUser_UserId(id, userId)
                .orElseThrow(() -> new RuntimeException("삭제할 버킷리스트가 존재하지 않습니다."));
        bucketListRepository.delete(bucketList);
    }

    public void togglePin(Long id) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        BucketList bucketList = bucketListRepository.findByBucketIdAndUser_UserId(id, userId)
                .orElseThrow(() -> new RuntimeException("버킷리스트가 존재하지 않습니다."));

        boolean isPinned = bucketList.getPinToTop();

        if (!isPinned && bucketListRepository.countByUser_UserIdAndPinToTopTrue(userId) >= 3) {
            throw new RuntimeException("고정 가능한 최대 개수는 3개입니다.");
        }

        bucketList.setPinToTop(!isPinned);
        bucketListRepository.save(bucketList);
    }

    public void achieve(Long id) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        BucketList bucketList = bucketListRepository.findByBucketIdAndUser_UserId(id, userId)
                .orElseThrow(() -> new RuntimeException("버킷리스트가 존재하지 않습니다."));

        bucketList.setStatus("done");
        bucketList.setAchievedAt(new Date());
        bucketListRepository.save(bucketList);
    }

    public List<BucketListHomeDto> getOngoingBucketListForHome() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return bucketListRepository.findByUser_UserIdOrderByCreatedAtDesc(userId)
                .stream()
                .filter(bucket -> bucket.getStatus().equals("ongoing"))
                .sorted(Comparator.comparing(BucketList::getTargetDate))
                .limit(3)
                .map(bucket -> BucketListHomeDto.builder()
                        .bucketId(bucket.getBucketId())
                        .title(bucket.getTitle())
                        .category(bucket.getCategory())
                        .targetDate(toDateStr(bucket.getTargetDate()))
                        .dday(calcDDay(bucket.getTargetDate()))
                        .build())
                .collect(Collectors.toList());
    }

    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            throw new RuntimeException("날짜 형식이 잘못되었습니다: yyyy-MM-dd 형식이어야 합니다.");
        }
    }

    private String toDateStr(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private String calcDDay(Date targetDate) {
        long diff = (targetDate.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24);
        if (diff > 0) return "D-" + diff;
        if (diff == 0) return "D-Day";
        return "D+" + Math.abs(diff);
    }
}
