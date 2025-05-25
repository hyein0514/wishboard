// src/main/java/com/guesthouse/wishboard/repository/BucketListRepository.java
package com.guesthouse.wishboard.repository;

import com.guesthouse.wishboard.entity.BucketList;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import java.util.List;

public interface BucketListRepository extends JpaRepository<BucketList, Long> {

    /* 1) 로그인 사용자가 소유한 버킷인지 */
    boolean existsByBucketIdAndUser_UserId(Long bucketId, String userId);

    /* 2) 같은 제목을 가진 버킷이 이미 존재하는지 */
    boolean existsByTitleAndUser_UserId(String title, String userId);

    /* 3) 특정 사용자의 버킷 목록 */
    List<BucketList> findByUser_UserIdOrderByPinToTopDescTargetDateAsc(String userId);
    Optional<BucketList> findByBucketIdAndUser_UserId(Long bucketId, String userId);

}
