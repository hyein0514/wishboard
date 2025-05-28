package com.guesthouse.wishboard.repository;

import com.guesthouse.wishboard.entity.BucketList;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TrophyRepository extends JpaRepository<BucketList, Long> {
    // 사용자의 완료된 버킷리스트만 가져오기
    List<BucketList> findByUser_IdAndStatus(Long id, String status);


}
