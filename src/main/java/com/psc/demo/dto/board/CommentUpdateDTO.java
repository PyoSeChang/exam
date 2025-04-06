package com.psc.demo.dto.board;


import com.psc.demo.domain.board.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateDTO {
    private Long id;
    private String nickname;
    private String content;

    public Comment toEntity() {
        return Comment.builder()
                .id(this.id)
                .nickname(this.nickname)
                .content(this.content)
                .build();
    }
    public Comment toDeleteEntity() {
        return Comment.builder()
                .id(this.id)
                .nickname("****")
                .content("삭제된 댓글입니다")
                .build();
    }
}
