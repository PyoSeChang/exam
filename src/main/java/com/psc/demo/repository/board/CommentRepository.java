package com.psc.demo.repository.board;

import com.psc.demo.domain.board.Board;
import com.psc.demo.domain.board.Comment;
import com.psc.demo.dto.board.CommentDTO;
import com.psc.demo.dto.board.CommentUpdateDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 게시글 ID로 댓글 목록 조회
    @Query("SELECT c FROM Comment c WHERE c.board.id = :boardId ORDER BY c.createdDate ASC")
    List<Comment> findAllByBoardId(@Param("boardId") Long boardId);

    @Query("SELECT c.nickname FROM Comment c WHERE c.id = :id")
    String findNicknameById(Long id);
    @Query("SELECT c.content FROM Comment c WHERE c.id = :id")
    String findContentById(Long id);

    @Query("SELECT c.board FROM Comment c WHERE c.id = :id")
    Board findBoardIdById(Long id);

    // 댓글 좋아요 증가
    @Modifying
    @Query("UPDATE Comment c SET c.likes = c.likes + 1 WHERE c.id = :commentId")
    void increaseLike(@Param("commentId") Long commentId);

    // 댓글 좋아요 감소
    @Modifying
    @Query("UPDATE Comment c SET c.likes = c.likes - 1 WHERE c.id = :commentId")
    void decreaseLike(@Param("commentId") Long commentId);

    // 댓글 싫어요 증가
    @Modifying
    @Query("UPDATE Comment c SET c.dislikes = c.dislikes + 1 WHERE c.id = :commentId")
    void increaseDisLike(@Param("commentId") Long commentId);

    // 댓글 싫어요 감소
    @Modifying
    @Query("UPDATE Comment c SET c.dislikes = c.dislikes - 1 WHERE c.id = :commentId")
    void decreaseDisLike(@Param("commentId") Long commentId);
}
