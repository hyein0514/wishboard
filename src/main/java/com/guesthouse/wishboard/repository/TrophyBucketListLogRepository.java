package com.guesthouse.wishboard.repository;

import com.guesthouse.wishboard.entity.BucketList_log;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TrophyBucketListLogRepository extends JpaRepository<BucketList_log, Long> {
    // bucketId로 모든 로그 가져오기
    List<BucketList_log> findByBucketIdOrderByCreatedAtAsc(Long bucketId);
}
