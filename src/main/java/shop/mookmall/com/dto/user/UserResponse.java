package shop.mookmall.com.dto.user;

import lombok.Getter;
import lombok.Setter;
import shop.mookmall.com.model.user.User;
import shop.mookmall.com.model.user.UserRole;

public class UserResponse {

    @Setter
    @Getter
    public static class JoinOutDTO {
        private Long id;
        private String username;

        public JoinOutDTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
        }
    }

    @Setter
    @Getter
    public static class LoginOutDTO {
        private Long id;
        private UserRole role;

        public LoginOutDTO(Long id, UserRole role) {
            this.id = id;
            this.role = role;
        }
    }
}
