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

            if (userData != null) {

                //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
                return new CustomUserDetail(userData);
            }

            return null;
        }

    }

