package shop.mookmall.com.model.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Table(name = "user_tb")
@Entity
public class User { // extends 시간설정 (상속)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 128)
    private String username;

    @Column(nullable = false, length = 128) // 패스워드 인코딩(BCrypt)
    private String password;

    @Column(nullable = false, length = 128)
    private String email;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    public User(Long id, String username, String password, String email, String role, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;

        if(role.equals("ROLE_SELLER")){
            this.role = UserRole.ROLE_SELLER;
        }else if(role.equals("ROLE_CUSTOMER")){
            this.role = UserRole.ROLE_CUSTOMER;
        }

        this.createdAt = createdAt;
    }
}