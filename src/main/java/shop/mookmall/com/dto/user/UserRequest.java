package shop.mookmall.com.dto.user;


import lombok.Getter;
import lombok.Setter;
import shop.mookmall.com.model.user.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRequest {
    @Setter
    @Getter
    public static class LoginInDTO {
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
        @NotEmpty
        private String email;
        @NotEmpty
        //@Pattern(regexp = "^[a-zA-Z0-9]{2,128}$", message = "영문/숫자 2~128자 이내로 작성해주세요")
        @Pattern(regexp = "^[0-9]{2,128}$", message = "숫자 2~128자 이내로 작성해주세요")
        private String password;
    }

    @Setter
    @Getter
    public static class JoinInDTO {

        @NotEmpty
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
        private String email;

        @Pattern(regexp = "^[a-zA-Z0-9]{2,128}$", message = "영문/숫자 2~128자 이내로 작성해주세요")
        @NotEmpty
        private String username;

        @NotEmpty
        @Size(min = 4, max = 128)
        private String password;

        @NotEmpty
        private String role;

        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .build();
        }
    }
}
