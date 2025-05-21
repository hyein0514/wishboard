package com.guesthouse.wishboard.service;

import com.guesthouse.wishboard.dto.UserDTO;
import com.guesthouse.wishboard.entity.User;
import com.guesthouse.wishboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JoinService {

        private final UserRepository userRepository;
        private final BCryptPasswordEncoder bCryptPasswordEncoder;

        public User joinProcess(UserDTO userDTO) {
            String userId = String.valueOf(userDTO.getUserId());
            String userPw = userDTO.getPassword();
            String nickName = userDTO.getNickName();

            User data = new User();

            data.setUserId(userId);
            data.setNickname(nickName);
            data.setPassword(bCryptPasswordEncoder.encode(userPw));

            return userRepository.save(data);

        }

//        public UserDTO updateUserPreferences(UserPreferencesDTO dto) {
//            User user = userRepository.findByUserId(dto.getUserId());
//
//            // User 객체가 null인 경우 예외 발생
//            if (user == null) {
//                throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다.");
//            }
//
//            user.setCountry(dto.getCountry());
//            user.setHometown(dto.getHometown());
//            user.setRole("ROLE_ADMIN");
//            userRepository.save(user);
//
//            tastePreferenceRepository.deleteByUser(user);
//
//            List<TastePreference> preferences = dto.getTastePreferences().stream()
//                    .map(tasteDTO -> {
//                        TastePreference tastePreference = new TastePreference();
//                        tastePreference.setUser(user);
//                        tastePreference.setTaste(tasteDTO.getTaste());
//                        tastePreference.setSpicyLevel(tasteDTO.getSpicyLevel());
//                        return tastePreference;
//                    })
//                    .collect(Collectors.toList());
//
//            tastePreferenceRepository.saveAll(preferences);
//
//            return UserMapper.toDTO(user);
//        }


    }

