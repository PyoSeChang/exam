package com.psc.demo.domain.board;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 게시글 ID (PK)

    @Column(nullable = false)
    private String subject;  // 게시글 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;  // 게시글 내용

    private String tags;  // 게시글 태그 (콤마로 구분)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;  // 게시판 종류 (예: NOTICE, FREE, QNA, STUDY)

    @Column(nullable = false)
    private String userid;  // 게시글 작성자 (사용자 ID)

    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL)
    private BoardMeta boardMeta;  // 게시글 메타 정보 (작성일, 조회수 등)

    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL)
    private BoardStatus boardStatus;  // 게시글 상태 정보 (활성화, 삭제 여부 등)

    // 게시판 종류를 정의한 Enum
    public enum Category {
        NOTICE, FREE, QNA, STUDY
    }
}
