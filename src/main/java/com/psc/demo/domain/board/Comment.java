package com.psc.demo.domain.board;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자 보호 수준 설정
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 1. 코멘트 아이디

    private String nickname; // 2. 작성자 (닉네임 기준)

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 3. 내용

    private int likes = 0;      // 7. 추천 수
    private int dislikes = 0;   // 7. 비추천 수

    private Long parentId;      // 5. 부모 코멘트
    private Long grandParentId; // 6. 조부모 코멘트

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false) // 실제 Board 엔티티의 id가 PK
    private Board board;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime modifiedDate;
}

