package com.guesthouse.wishboard.service;

import com.guesthouse.wishboard.entity.User;
import com.guesthouse.wishboard.jwt.CustomUserDetail;
import com.guesthouse.wishboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService{

        private final UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

            //DB에서 조회
            User userData = userRepository.findAllByUserId(userId);
            System.out.println("DB에서 찾은 userData: " + userData);

            if (userData == null) {
                throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다: " + userId);
            }

            return new CustomUserDetail(userData);
        }

    }

