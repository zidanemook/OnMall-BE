package shop.mookmall.com.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import shop.mookmall.com.core.auth.jwt.MyJwtProvider;
import shop.mookmall.com.core.auth.session.MyUserDetails;
import shop.mookmall.com.core.exception.Exception400;
import shop.mookmall.com.core.exception.Exception401;
import shop.mookmall.com.core.exception.Exception500;
import shop.mookmall.com.dto.user.UserRequest;
import shop.mookmall.com.dto.user.UserResponse;
import shop.mookmall.com.model.token.RefreshTokenEntity;
import shop.mookmall.com.model.token.TokenRepository;
import shop.mookmall.com.model.user.User;
import shop.mookmall.com.model.user.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    private final TokenRepository tokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse.JoinOutDTO Signup(UserRequest.JoinInDTO joinInDTO) {

        Optional<User> userByEmail = userRepository.findByEmail(joinInDTO.getEmail());
        if (userByEmail.isPresent()) {
            throw new Exception400("email", "해당 이메일 주소는 사용중입니다");
        }

        String encPassword = passwordEncoder.encode(joinInDTO.getPassword()); // 60Byte
        joinInDTO.setPassword(encPassword);

        try {
            User userPS = userRepository.save(joinInDTO.toEntity());
            return new UserResponse.JoinOutDTO(userPS);
        } catch (Exception e) {
            throw new Exception500("회원가입 실패 : " + e.getMessage());
        }
    }

    public Pair<String, String> Login(UserRequest.LoginInDTO loginInDTO) {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(loginInDTO.getEmail(), loginInDTO.getPassword());
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();

            String accessjwt = MyJwtProvider.createAccess(myUserDetails.getUser());
            Pair<String, RefreshTokenEntity> rtInfo = MyJwtProvider.createRefresh(myUserDetails.getUser());

            //로그인 성공하면 액세스 토큰, 리프레시 토큰 발급. 리프레시 토큰의 uuid은 DB에 저장
            tokenRepository.save(rtInfo.getSecond());

            return Pair.of(accessjwt, rtInfo.getFirst());
        } catch (Exception e) {
            throw new Exception401("인증되지 않았습니다");
        }
    }

    public UserResponse.LoginOutDTO SearchEmail(String email) {
        User findUser = userRepository.findByEmail(email).orElseThrow(
                () -> new Exception400("email", "해당 유저를 찾을 수 없습니다.")
        );
        UserResponse.LoginOutDTO loginOutDTO = new UserResponse.LoginOutDTO(findUser.getId(), findUser.getRole());
        return loginOutDTO;
    }
}
