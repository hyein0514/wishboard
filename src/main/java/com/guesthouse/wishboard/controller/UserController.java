package com.guesthouse.wishboard.controller;

import com.guesthouse.wishboard.dto.MyPageDTO;
import com.guesthouse.wishboard.dto.UserDTO;
import com.guesthouse.wishboard.entity.Notification;
import com.guesthouse.wishboard.entity.User;
import com.guesthouse.wishboard.global.ApiResponsTemplate;
import com.guesthouse.wishboard.jwt.CustomUserDetail;
import com.guesthouse.wishboard.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8081") // http://localhost:8081는 React Native Web 실행 주소
@Tag(name = "UserController",description = "로그인, 회원가입 관련 API")
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

        private final UserService joinService;


    public UserController(UserService joinService) {
            this.joinService = joinService;
        }

        @PostMapping("/user")
        public ResponseEntity<ApiResponsTemplate<?>> createUser(@RequestBody UserDTO userDTO) {
            try {
                User savedUser = joinService.joinProcess(userDTO);

                return ResponseEntity.ok(ApiResponsTemplate.success(savedUser));
            } catch (Exception e) {
                log.error("회원가입 실패", e);
                return ResponseEntity.status(500).body(new ApiResponsTemplate<>("UPLOAD_FAILED", e.getMessage(), null));
            }
        }

        
        @GetMapping("/mypage")
        public ResponseEntity<Map<String, String>> getMyPageInfo(@AuthenticationPrincipal CustomUserDetail userDetail) {
            Long userId = userDetail.getUser().getId(); // DB 기준의 PK
            String nickname = joinService.getNicknameByUserId(userId);

            Map<String, String> response = new HashMap<>();
            response.put("nickname", nickname);
            return ResponseEntity.ok(response);
        }

        @PostMapping("/update")
        public ResponseEntity<ApiResponsTemplate<?>> updateUserPreferences(@RequestBody UserDTO dto, @AuthenticationPrincipal CustomUserDetail users) {
            try {
                joinService.updateUserPreferences(dto,users);
                return ResponseEntity.ok(ApiResponsTemplate.success(200));
            } catch (Exception e) {
               return ResponseEntity.status(500).body(new ApiResponsTemplate<>("UPLOAD_FAILED", e.getMessage(), null));
            }
        }

        @GetMapping("/selectAlam")
        public ResponseEntity<ApiResponsTemplate<List<Notification>>> selectAlam(@AuthenticationPrincipal CustomUserDetail users) {
            try {
                return ResponseEntity.ok(ApiResponsTemplate.success(joinService.selectAlam(users)));
            } catch (Exception e) {
                return ResponseEntity.status(500).body(new ApiResponsTemplate<>("UPLOAD_FAILED", e.getMessage(), null));
            }
        }

        @GetMapping("/mycomment")
        public ResponseEntity<ApiResponsTemplate<List<MyPageDTO>>> selectComment(@AuthenticationPrincipal CustomUserDetail users) {
            try {
                return ResponseEntity.ok(ApiResponsTemplate.success(joinService.selectCommunityByCommnet(users)));
            } catch (Exception e) {
                return ResponseEntity.status(500).body(new ApiResponsTemplate<>("UPLOAD_FAILED", e.getMessage(), null));
            }
        }


        @GetMapping("/myLike")
        public ResponseEntity<ApiResponsTemplate<List<MyPageDTO>>> selectLike(@AuthenticationPrincipal CustomUserDetail users) {
            try {
                return ResponseEntity.ok(ApiResponsTemplate.success(joinService.selectCommunityByLike(users)));
            } catch (Exception e) {
                return ResponseEntity.status(500).body(new ApiResponsTemplate<>("UPLOAD_FAILED", e.getMessage(), null));
            }
        }

        @GetMapping("/myWrite")
        public ResponseEntity<ApiResponsTemplate<List<MyPageDTO>>> selectWrite(@AuthenticationPrincipal CustomUserDetail users) {
            try {
                return ResponseEntity.ok(ApiResponsTemplate.success(joinService.selectCommunityByWrite(users)));
            } catch (Exception e) {
                return ResponseEntity.status(500).body(new ApiResponsTemplate<>("UPLOAD_FAILED", e.getMessage(), null));
            }
        }

    }


