package Integration.integration.service;

import Integration.integration.entity.Member;
import Integration.integration.exception.CustomException;
import Integration.integration.dto.request.LoginRequest;
import Integration.integration.dto.request.RegisterRequest;
import Integration.integration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException("EMAIL_EXISTS", "이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT);
        }

        Member user = new Member();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());

        userRepository.save(user);
    }

    public String login(LoginRequest request) {
        Member user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException("USER_NOT_FOUND", "이메일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException("INVALID_PASSWORD", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

        return "fake-jwt-token"; // 이후 JWT 발급 로직 연결
    }

    public String findEmailByNickname(String nickname) {
        return userRepository.findAll().stream()
                .filter(u -> u.getNickname().equals(nickname))
                .map(Member::getEmail)
                .findFirst()
                .orElseThrow(() -> new CustomException("EMAIL_NOT_FOUND", "해당 닉네임의 이메일이 없습니다.", HttpStatus.NOT_FOUND));
    }

    public void sendResetPassword(String email) {
        Member user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("EMAIL_NOT_FOUND", "이메일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        // 임시 비밀번호 생성 or 이메일 전송 로직
        System.out.println("비밀번호 초기화 메일 전송됨");
    }
}
