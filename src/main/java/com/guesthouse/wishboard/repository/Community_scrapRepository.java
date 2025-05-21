package com.guesthouse.wishboard.repository;

import com.guesthouse.wishboard.entity.Community_scrap;
import com.guesthouse.wishboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Community_scrapRepository
        extends JpaRepository<Community_scrap, Long> {

    boolean existsByCommunityNameAndUser(String communityName, User user);

    Optional<Community_scrap> findByCommunityNameAndUser(String communityName, User user);

    Page<Community_scrap> findByUser(User user, Pageable pageable);
}

