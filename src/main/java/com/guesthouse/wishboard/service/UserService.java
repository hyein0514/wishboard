package com.guesthouse.wishboard.service;
import com.guesthouse.wishboard.dto.MyPageDTO;
import com.guesthouse.wishboard.dto.UserDTO;
import com.guesthouse.wishboard.entity.*;
import com.guesthouse.wishboard.jwt.CustomUserDetail;
import com.guesthouse.wishboard.repository.CommentRepository;
import com.guesthouse.wishboard.repository.NotificationRepository;
import com.guesthouse.wishboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class UserService {

        private final UserRepository userRepository;
        private final BCryptPasswordEncoder bCryptPasswordEncoder;
        private final NotificationRepository notificationRepository;
        private final CommentRepository commentRepository;

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

        public List<MyPageDTO> selectCommunityByCommnet(CustomUserDetail users) {
            List<Comment> comments = commentRepository.findByuserIdAboutComment(users.getUser().getId());
            List<MyPageDTO> myPageDTOS = new ArrayList<>();
            for(Comment comment : comments){
                MyPageDTO myPageDTO = MyPageDTO.builder()
                    .communityId(comment.getCommunityId())
                    .title(comment.getCommunity().getTitle())
                    .type(comment.getCommunity().getType())
                    .communityDiversity(comment.getCommunity().getCommunityDiversity())
                    .content(comment.getCommunity().getContent())
                    .commentNum(comments.size())
                    .createdAt(comment.getCommunity().getCreatedAt())
                    .writerNickName(comment.getCommunity().getUser().getNickname())
                    .img(!comment.getCommunity().getImages().isEmpty()
                            ? comment.getCommunity().getImages().get(0).getImageUrl()
                            : null)
                    .likeNum(comment.getCommunity().getLikes().size())
                    .communityType(comment.getCommunity().getCommunityType()) // ✅ 추가
                    .build();

                myPageDTOS.add(myPageDTO);
            }

            return myPageDTOS;
        }

    public List<MyPageDTO> selectCommunityByLike(CustomUserDetail users) {
        List<Community> communities = commentRepository.findByuserIdAboutLike(users.getUser().getId());

        List<MyPageDTO> myPageDTOS = new ArrayList<>();
        for(Community community : communities){
            MyPageDTO myPageDTO = MyPageDTO.builder()
                    .communityId(community.getCommunityId())
                    .title(community.getTitle())
                    .communityType(community.getCommunityType())
                    .communityDiversity(community.getCommunityDiversity())
                    .content(community.getContent())
                    .commentNum(community.getComments().size())
                    .createdAt(community.getCreatedAt())
                    .writerNickName(community.getUser().getNickname())
                    .img(!community.getImages().isEmpty()
                            ? community.getImages().get(0).getImageUrl()
                            : null)
                    .likeNum(community.getLikes().size())
                    .build();

            myPageDTOS.add(myPageDTO);
        }

        return myPageDTOS;
    }

    public List<MyPageDTO> selectCommunityByWrite(CustomUserDetail users) {
        List<Community> communities = commentRepository.findByUserIdAboutPost(users.getUser().getId());

        List<MyPageDTO> myPageDTOS = new ArrayList<>();
        for(Community community : communities){
            MyPageDTO myPageDTO = MyPageDTO.builder()
                    .communityId(community.getCommunityId())
                    .title(community.getTitle())
                    .communityType(community.getCommunityType())
                    .communityDiversity(community.getCommunityDiversity())
                    .content(community.getContent())
                    .commentNum(community.getComments().size())
                    .createdAt(community.getCreatedAt())
                    .writerNickName(community.getUser().getNickname())
                    .img(!community.getImages().isEmpty()
                            ? community.getImages().get(0).getImageUrl()
                            : null)
                    .likeNum(community.getLikes().size())
                    .build();

            myPageDTOS.add(myPageDTO);
        }

        return myPageDTOS;
    }

    }

