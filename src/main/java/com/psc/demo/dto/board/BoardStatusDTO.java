package com.psc.demo.dto.board;

import lombok.Data;

@Data
public class BoardStatusDTO {

    private Long id;

    private boolean active;  // 게시글 활성화 여부
    private boolean deleted; // 게시글 삭제 여부
    private boolean hidden;  // 게시글 숨김 여부

    private int viewCount;  // 조회수
    private int likeCount;  // 좋아요 수
    private int dislikeCount;  // 싫어요 수
    private int commentCount;  // 댓글 수
}
