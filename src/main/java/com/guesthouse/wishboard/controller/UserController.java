package com.guesthouse.wishboard.controller;

import com.guesthouse.wishboard.dto.UserDTO;
import com.guesthouse.wishboard.entity.User;
import com.guesthouse.wishboard.global.ApiResponsTemplate;
import com.guesthouse.wishboard.service.JoinService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "UserController",description = "로그인, 회원가입 관련 API")
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

        private final JoinService joinService;


    public UserController(JoinService joinService) {
            this.joinService = joinService;
        }

        @PostMapping("/user")
        public ResponseEntity<ApiResponsTemplate<?>> createUser(@RequestBody UserDTO userDTO) {
            try {
                User savedUser = joinService.joinProcess(userDTO);

                return ResponseEntity.ok(ApiResponsTemplate.success(savedUser));
            } catch (Exception e) {
                log.error("회원가입 실패", e);
                return ResponseEntity.status(400).body(new ApiResponsTemplate<>("UPLOAD_FAILED", e.getMessage(), null));
            }
        }


//        @PostMapping("/preferences")
//        public ResponseEntity<ApiResponseTemplate<UserDTO>> updateUserPreferences(@RequestBody UserPreferencesDTO dto) {
//            try {
//                UserDTO updatedUser = joinService.updateUserPreferences(dto);
//                return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.CREATED, updatedUser));
//            } catch (IllegalArgumentException e) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(ApiResponseTemplate.error(ErrorCode.NOT_FOUND));
//            } catch (Exception e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body(ApiResponseTemplate.error(ErrorCode.INTERNAL_SERVER_ERROR));
//            }
//        }

    }


