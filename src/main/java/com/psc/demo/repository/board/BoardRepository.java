package com.psc.demo.repository.board;

import com.psc.demo.domain.board.Board;
import com.psc.demo.dto.board.BoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 카테고리별 게시글을 최신순으로 정렬
    @Query("SELECT b FROM Board b WHERE b.category = :category ORDER BY b.boardMeta.regDate DESC")
    Page<Board> findByCategory(Board.Category category, Pageable pageable);

    // 카테고리별 제목에 'keyword'를 포함하는 게시글을 최신순으로 정렬
    @Query("SELECT b FROM Board b WHERE b.category = :category AND b.title LIKE %:keyword% ORDER BY b.boardMeta.regDate DESC")
    Page<Board> findByCategoryAndTitleContaining(Board.Category category, String keyword, Pageable pageable);

    @Query("SELECT b FROM Board b JOIN b.member m WHERE b.category = :category AND m.nickname LIKE %:keyword% ORDER BY b.boardMeta.regDate DESC")
    Page<Board> findByCategoryAndNicknameContaining(@Param("category") Board.Category category,
                                                    @Param("keyword") String keyword,
                                                    Pageable pageable);

    @Query("SELECT b FROM Board b JOIN b.boardMeta m WHERE b.category = :category AND m.tags LIKE CONCAT('%', :keyword, '%') ORDER BY b.boardMeta.regDate DESC")
    Page<Board> findByCategoryAndTagsContaining(@Param("category") Board.Category category,
                                                @Param("keyword") String keyword,
                                                Pageable pageable);

    // 전체 게시글 조회 시 최신순 정렬
    @Query("SELECT b FROM Board b ORDER BY b.boardMeta.regDate DESC")
    Page<Board> findAll(Pageable pageable); // 기본 전체 조회

    @Query("SELECT b FROM Board b WHERE b.title LIKE %:keyword% ORDER BY b.boardMeta.regDate DESC")
    Page<Board> findByTitleContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT b FROM Board b JOIN b.member m WHERE m.nickname LIKE %:keyword% ORDER BY b.boardMeta.regDate DESC")
    Page<Board> findByNicknameContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT b FROM Board b JOIN b.boardMeta m WHERE m.tags LIKE %:keyword% ORDER BY b.boardMeta.regDate DESC")
    Page<Board> findByTagsContaining(@Param("keyword") String keyword, Pageable pageable);




}
