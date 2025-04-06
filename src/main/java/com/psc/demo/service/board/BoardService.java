package com.psc.demo.service.board;

import com.psc.demo.domain.board.Board;
import com.psc.demo.dto.board.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {
    // 보드 삽입
    void createBoard(BoardViewDTO dto);
    // 보드 전체 들고오기
    Page<BoardListDTO> getAllBoards(Board.Category category, String keyword, String searchType, Pageable pageable);
    // 보드 하나 가져오기
    BoardViewDTO getBoardById(Long id);
    // 보드 수정하기
    BoardDTO updateBoardById(BoardDTO dto, Long id);
    // 보드 삭제하기
    void deleteBoardById(Long id);
    // 댓글 입력하기
    void createComment(CommentDTO dto);
    // 댓글 불러오기
    List<CommentDTO> getCommentsByBoardId(Long id);
    // 댓글 삭제하기
    void deleteComment(CommentUpdateDTO dto);
    // 댓글 수정하기
    void updateComment(CommentUpdateDTO dto);
    // 조회수 카운트
    void incrementViewCount(Long boarId, HttpSession session);
}
