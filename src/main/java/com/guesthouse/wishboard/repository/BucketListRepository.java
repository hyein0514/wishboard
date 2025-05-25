package com.guesthouse.wishboard.repository;

import com.guesthouse.wishboard.entity.BucketList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BucketListRepository extends JpaRepository<BucketList, Long> {
    boolean existsByBucketIdAndUser_UserId(Long bucketId, String userId);
    boolean existsByTitleAndUser_UserId(String title, String userId);
    List<BucketList> findByUser_UserIdOrderByPinToTopDescTargetDateAsc(String userId);
    Optional<BucketList> findByBucketIdAndUser_UserId(Long bucketId, String userId);
    long countByUser_UserIdAndPinToTopTrue(String userId);
}

