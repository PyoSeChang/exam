package com.psc.demo.domain.member;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userid;  // 로그인 ID

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;  // 회원 유형 (예: USER, ADMIN)

    private LocalDateTime regDate;

    @PrePersist
    protected void onCreate() {
        this.regDate = LocalDateTime.now();
    }

    @Builder
    public Member(String userid, String password, String nickname, Role role) {
        this.userid = userid;
        this.password = password;
        this.nickname = nickname;
        this.role = (role == null) ? Role.USER : role; // 기본값을 여기에 설정
    }

    public enum Role {
        USER, ADMIN
    }
}
