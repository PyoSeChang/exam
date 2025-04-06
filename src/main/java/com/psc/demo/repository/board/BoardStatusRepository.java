package com.psc.demo.repository.board;


import com.psc.demo.domain.board.BoardStatus;
import com.psc.demo.dto.board.BoardStatusDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardStatusRepository extends JpaRepository<BoardStatus, Long> {

    // 게시글 좋아요 증가
    @Modifying
    @Query("UPDATE BoardStatus b SET b.likeCount = b.likeCount + 1 WHERE b.id = :boardId")
    void increaseLike(@Param("boardId") Long boardId);

    // 게시글 좋아요 감소
    @Modifying
    @Query("UPDATE BoardStatus b SET b.likeCount = b.likeCount - 1 WHERE b.id = :boardId")
    void decreaseLike(@Param("boardId") Long boardId);

    // 게시글 싫어요 증가
    @Modifying
    @Query("UPDATE BoardStatus b SET b.dislikeCount = b.dislikeCount + 1 WHERE b.id = :boardId")
    void increaseDisLike(@Param("boardId") Long boardId);

    // 게시글 싫어요 감소
    @Modifying
    @Query("UPDATE BoardStatus b SET b.dislikeCount = b.dislikeCount - 1 WHERE b.id = :boardId")
    void decreaseDisLike(@Param("boardId") Long boardId);

}

