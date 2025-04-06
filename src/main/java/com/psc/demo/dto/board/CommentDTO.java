package com.psc.demo.dto.board;

import com.psc.demo.domain.board.Board;
import com.psc.demo.domain.board.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class CommentDTO {

    private Long id;
    private String nickname;
    private String content;
    private int likes;
    private int dislikes;
    private Long parentId;
    private Long grandParentId;
    private Long boardId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String referencedNickname;
    private String referencedContent;

    // Entity → DTO 변환
    public static CommentDTO fromEntity(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setNickname(comment.getNickname());
        dto.setContent(comment.getContent());
        dto.setLikes(comment.getLikes());
        dto.setDislikes(comment.getDislikes());
        dto.setParentId(comment.getParentId());
        dto.setGrandParentId(comment.getGrandParentId());
        dto.setBoardId(comment.getBoard().getId());
        dto.setCreatedDate(comment.getCreatedDate());
        dto.setModifiedDate(comment.getModifiedDate());
        return dto;
    }

    // DTO → Entity 변환
    public Comment toEntity(Board board) {
        return Comment.builder()
                .id(this.id)
                .nickname(this.nickname)
                .content(this.content)
                .likes(this.likes)
                .dislikes(this.dislikes)
                .parentId(this.parentId)
                .grandParentId(this.grandParentId)
                .board(board)
                .build();
    }

}
