package com.guesthouse.wishboard.controller;

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

import java.util.List;


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

    }


