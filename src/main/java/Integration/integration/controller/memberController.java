package Integration.integration.controller;

import Integration.integration.dto.request.LoginRequest;
import Integration.integration.dto.request.RegisterRequest;
import Integration.integration.dto.response.ApiResponse;
import Integration.integration.service.memberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class memberController {

    private final memberService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody @Valid RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody @Valid LoginRequest request) {
        String token = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success(Map.of("accessToken", token)));
    }

    @GetMapping("/find-email")
    public ResponseEntity<ApiResponse<?>> findEmail(@RequestParam String nickname) {
        String email = userService.findEmailByNickname(nickname);
        return ResponseEntity.ok(ApiResponse.success(Map.of("email", email)));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<?>> resetPassword(@RequestParam String email) {
        userService.sendResetPassword(email);
        return ResponseEntity.ok(ApiResponse.success("비밀번호 재설정 메일이 전송되었습니다."));
    }
}
