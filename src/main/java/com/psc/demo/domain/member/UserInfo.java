package com.psc.demo.domain.member;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String gender; // 예: "M", "F"

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String tel; // 선택 사항

    @Column(updatable = false)
    private LocalDate createdDate = LocalDate.now();

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDate.now();
    }
}
