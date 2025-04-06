package com.psc.demo.dto.board;

import com.psc.demo.domain.board.Board;
import com.psc.demo.domain.board.BoardMeta;
import com.psc.demo.domain.board.BoardStatus;
import jdk.jshell.Snippet;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class BoardListDTO {

        private Long id;                     // 게시글 ID (상세보기 이동용)
        private String title;               // 제목
        private String userid;              // 작성자
        private String nickname;            // 닉네임
        private Board.Category category;    // 카테고리 (ENUM)

        private int viewCount;              // 조회수
        private int likeCount;              // 좋아요 수

        private String tags;                // 태그 (검색용으로만 사용)

        private LocalDateTime regDate;      // 작성일자
        private LocalDateTime updatedDate;  // 수정일자 (출력할 날짜 기준은 컨트롤러에서 정해도 되고요)
        private int commentCount;

        public static BoardListDTO from(Board board, BoardMeta meta, BoardStatus status) {
                return BoardListDTO.builder()
                        .id(board.getId())
                        .title(board.getTitle())
                        .category(board.getCategory())
                        .userid(board.getUserid())
                        .viewCount(status.getViewCount())
                        .likeCount(status.getLikeCount())
                        .tags(meta.getTags())
                        .regDate(meta.getRegDate())
                        .build();
        }




}
