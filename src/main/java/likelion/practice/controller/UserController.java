package likelion.practice.controller;

import likelion.practice.dto.UserDTO;
import likelion.practice.entity.User;
import likelion.practice.security.JwtTokenProvider;
import likelion.practice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    // 회원가입 API
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDTO userDTO) {
        User registeredUser = userService.registerUser(userDTO);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody UserDTO userDTO) {
        try {
            // 사용자 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getUserId(), userDTO.getPassword())
            );

            // 인증 성공 시 JWT 토큰 생성
            String jwtToken = jwtTokenProvider.createToken(authentication.getName());

            // 응답으로 토큰을 Map 형태로 반환
            Map<String, String> response = new HashMap<>();
            response.put("token", jwtToken);

            return ResponseEntity.ok(response);

        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password.");
        }
    }

    // ID 중복 확인 API
    @GetMapping("/check-id")
    public ResponseEntity<String> checkUserIdDuplicate(@RequestParam String userId) {
        boolean isDuplicate = userService.isUserIdDuplicate(userId);
        if (isDuplicate) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 아이디입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 아이디입니다.");
        }
    }

    // 마이페이지 - 회원 정보 조회
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        UserDTO userDTO = userService.getUserProfile(userDetails.getUsername());
        return ResponseEntity.ok(userDTO);
    }

    // 마이페이지 - 프로필 사진 및 이름 수정
    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateUserProfile(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestBody UserDTO updatedUserDTO) {
        UserDTO userDTO = userService.updateUserProfile(userDetails.getUsername(), updatedUserDTO);
        return ResponseEntity.ok(userDTO);
    }
}
