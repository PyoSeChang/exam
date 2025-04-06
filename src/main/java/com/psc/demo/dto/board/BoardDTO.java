package com.psc.demo.dto.board;

import com.psc.demo.domain.board.Board;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardDTO {

    private Long id;
    private String title;
    private String content;
    private Board.Category category;
    private String userid;

    public static BoardDTO fromEntity(Board board) {
        BoardDTO dto = new BoardDTO();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setUserid(board.getUserid());
        return dto;

    }

    public Board toBoardEntity() {
        return Board.builder()
                .title(this.title)
                .content(this.content)
                .category(this.category)
                .userid(this.userid)
                .build();
    }

}
