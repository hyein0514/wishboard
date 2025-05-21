package com.guesthouse.wishboard.service;
import com.guesthouse.wishboard.dto.UserDTO;
import com.guesthouse.wishboard.entity.Keyword;
import com.guesthouse.wishboard.entity.Notification;
import com.guesthouse.wishboard.entity.User;
import com.guesthouse.wishboard.jwt.CustomUserDetail;
import com.guesthouse.wishboard.repository.NotificationRepository;
import com.guesthouse.wishboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;


@RequiredArgsConstructor
@Service
public class UserService {

        private final UserRepository userRepository;
        private final BCryptPasswordEncoder bCryptPasswordEncoder;
        private final NotificationRepository notificationRepository;

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

        public void updateUserPreferences(UserDTO dto, CustomUserDetail users) {
            User user = userRepository.findAllByUserId(users.getUsername());

            // User 객체가 null인 경우 예외 발생
            if (user == null) {
                throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다.");
            }

            user.setNickname(dto.getNickName());
            user.setPassword(dto.getPassword());
            userRepository.save(user);

        }

        public List<Notification> selectAlam(CustomUserDetail users) {
           return notificationRepository.findAllByUserId(users.getUser().getId());
        }

    }

