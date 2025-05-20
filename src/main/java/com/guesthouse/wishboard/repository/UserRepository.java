package com.guesthouse.wishboard.repository;

import com.guesthouse.wishboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByUserId(String userId);
    User findAllByUserId(String userId);
}
