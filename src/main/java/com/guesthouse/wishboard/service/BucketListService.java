package com.guesthouse.wishboard.service;

import com.guesthouse.wishboard.dto.BucketListRequestDto;
import com.guesthouse.wishboard.entity.BucketList;
import com.guesthouse.wishboard.entity.User;
import com.guesthouse.wishboard.repository.BucketListRepository;
import com.guesthouse.wishboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            throw new RuntimeException("날짜 형식이 잘못되었습니다: yyyy-MM-dd 형식이어야 합니다.");
        }
    }
}
