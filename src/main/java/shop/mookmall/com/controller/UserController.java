package shop.mookmall.com.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shop.mookmall.com.core.auth.jwt.MyJwtProvider;
import shop.mookmall.com.dto.ResponseDTO;
import shop.mookmall.com.dto.user.UserRequest;
import shop.mookmall.com.dto.user.UserResponse;
import shop.mookmall.com.service.RefreshService;
import shop.mookmall.com.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    private final RefreshService refreshService;

    @PostMapping("/signup")
    public ResponseEntity<?> join(@RequestBody @Valid UserRequest.JoinInDTO joinInDTO, Errors errors) {
        UserResponse.JoinOutDTO joinOutDTO = userService.Signup(joinInDTO);
        ResponseDTO<?> responseDTO = new ResponseDTO<>(joinOutDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest.LoginInDTO loginInDTO, Errors errors) {
        Pair<String, String > tokenInfo = userService.Login(loginInDTO);
        UserResponse.LoginOutDTO loginOutDTO = userService.SearchEmail(loginInDTO.getEmail());

        ResponseDTO<?> responseDTO = new ResponseDTO<>(loginOutDTO);
        return ResponseEntity.ok()
                .header(MyJwtProvider.HEADER, tokenInfo.getFirst())
                .header(MyJwtProvider.HEADER_REFRESH, tokenInfo.getSecond())
                .body(responseDTO);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){

        refreshService.revokeRefreshToken(request);

        return ResponseEntity.ok().build();
    }
}
