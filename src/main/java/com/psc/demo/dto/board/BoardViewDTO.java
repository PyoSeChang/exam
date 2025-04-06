package com.psc.demo.dto.board;

import com.psc.demo.domain.board.Board;
import com.psc.demo.domain.board.BoardMeta;
import com.psc.demo.domain.board.BoardStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardViewDTO {
    private Long id;
    private String title;
    private String content;
    private Board.Category category;
    private String userid;
    private String nickname;

    private String tags;
    private LocalDateTime regDate;  // 작성일
    private LocalDateTime updatedDate;  // 수정일

    private boolean active;  // 게시글 활성화 여부
    private boolean deleted; // 게시글 삭제 여부
    private boolean hidden;  // 게시글 숨김 여부

    private int viewCount;  // 조회수
    private int likeCount;  // 좋아요 수
    private int dislikeCount;  // 싫어요 수
    private int commentCount;  // 댓글 수

    public static BoardViewDTO from(Board board, BoardMeta meta, BoardStatus status) {
        BoardViewDTO dto = new BoardViewDTO();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setCategory(board.getCategory());
        dto.setUserid(board.getUserid());
        dto.setTags(meta.getTags());
        dto.setRegDate(meta.getRegDate());
        dto.setUpdatedDate(meta.getUpdatedDate());
        dto.setActive(status.isActive());
        dto.setDeleted(status.isDeleted());
        dto.setHidden(status.isHidden());
        dto.setViewCount(status.getViewCount());
        dto.setLikeCount(status.getLikeCount());
        dto.setDislikeCount(status.getDislikeCount());
        dto.setCommentCount(status.getCommentCount());
        return dto;
    }

    // 게시글 본문 엔티티로 변환
    public Board toBoardEntity() {
        return Board.builder()
                .title(this.title)
                .content(this.content)
                .category(this.category)
                .userid(this.userid)
                .build();
    }

    // 게시글 메타정보 엔티티로 변환
    public BoardMeta toMetaEntity(Board board) {
        return BoardMeta.builder()
                .board(board)  // 연관관계 객체 주입 (MapsId 사용 시 필수!)
                .tags(this.tags)
                .regDate(this.regDate != null ? this.regDate : LocalDateTime.now())
                .updatedDate(this.updatedDate != null ? this.updatedDate : LocalDateTime.now())
                .build();
    }

    // 게시글 상태정보 엔티티로 변환
    public BoardStatus toStatusEntity(Board board) {
        return BoardStatus.builder()
                .board(board)  // 연관관계 객체 주입
                .active(this.active)
                .deleted(this.deleted)
                .hidden(this.hidden)
                .viewCount(this.viewCount)
                .likeCount(this.likeCount)
                .dislikeCount(this.dislikeCount)
                .commentCount(this.commentCount)
                .build();
    }

    // boardDTO 다운캐스팅
    public BoardDTO toBoardDTO() {
        BoardDTO dto = new BoardDTO();
        dto.setId(this.id);
        dto.setTitle(this.title);
        dto.setContent(this.content);
        dto.setCategory(this.category);
        dto.setUserid(this.userid);
        return dto;
    }


}
