package com.psc.demo.domain.board;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 상태 ID (PK)

    @OneToOne
    @MapsId
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Board board;  // 게시글 정보와 연결

    private boolean active;  // 게시글 활성화 여부
    private boolean deleted; // 게시글 삭제 여부
    private boolean hidden;  // 게시글 숨김 여부

    private int viewCount;  // 조회수
    private int likeCount;  // 좋아요 수
    private int dislikeCount;  // 싫어요 수
    private int commentCount;  // 댓글 수

    @PrePersist
    protected void onCreate() {
        this.active = true;  // 기본적으로 게시글은 활성화 상태
        this.deleted = false;  // 기본적으로 게시글은 삭제되지 않음
        this.hidden = false;  // 기본적으로 게시글은 숨겨져 있지 않음
        this.viewCount = 0;  // 기본 조회수 0
        this.likeCount = 0;  // 기본 좋아요 수 0
        this.dislikeCount = 0;  // 기본 싫어요 수 0
        this.commentCount = 0;  // 기본 댓글 수 0
    }
}

