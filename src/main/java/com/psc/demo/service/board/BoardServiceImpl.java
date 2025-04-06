package com.psc.demo.service.board;

import com.psc.demo.domain.board.Board;
import com.psc.demo.domain.board.BoardMeta;
import com.psc.demo.domain.board.BoardStatus;
import com.psc.demo.domain.board.Comment;
import com.psc.demo.dto.board.*;
import com.psc.demo.repository.board.BoardMetaRepository;
import com.psc.demo.repository.board.BoardRepository;
import com.psc.demo.repository.board.BoardStatusRepository;
import com.psc.demo.repository.board.CommentRepository;
import com.psc.demo.repository.member.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardMetaRepository boardMetaRepository;
    private final BoardStatusRepository boardStatusRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public void createBoard(BoardViewDTO dto) {
        Board board = dto.toBoardEntity();
        boardRepository.save(board);

        BoardMeta meta = dto.toMetaEntity(board);
        boardMetaRepository.save(meta);

        BoardStatus status = dto.toStatusEntity(board);
        boardStatusRepository.save(status);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BoardListDTO> getAllBoards(Board.Category category, String keyword, String searchType, Pageable pageable) {
        Page<Board> boardPage;

        if (keyword != null && !keyword.isBlank()) {
            switch (searchType) {
                case "title" -> boardPage = (category != null)
                        ? boardRepository.findByCategoryAndTitleContaining(category, keyword, pageable)
                        : boardRepository.findByTitleContaining(keyword, pageable);  // 카테고리 없이 제목 검색
                case "nickname" -> boardPage = (category != null)
                        ? boardRepository.findByCategoryAndNicknameContaining(category, keyword, pageable)
                        : boardRepository.findByNicknameContaining(keyword, pageable);  // 카테고리 없이 작성자 검색
                case "tag" -> boardPage = (category != null)
                        ? boardRepository.findByCategoryAndTagsContaining(category, keyword, pageable)
                        : boardRepository.findByTagsContaining(keyword, pageable);  // 카테고리 없이 태그 검색
                default -> boardPage = (category != null)
                        ? boardRepository.findByCategory(category, pageable)
                        : boardRepository.findAll(pageable);  // 카테고리 없이 전체 게시글 검색
            }
        } else {
            boardPage = (category != null)
                    ? boardRepository.findByCategory(category, pageable)  // 카테고리 필터링
                    : boardRepository.findAll(pageable);  // 전체 게시글
        }

        // Board + BoardMeta + BoardStatus → BoardListDTO 변환
        return boardPage.map(board -> {
            BoardMeta meta = boardMetaRepository.findById(board.getId()).orElse(null);
            BoardStatus status = boardStatusRepository.findById(board.getId()).orElse(null);
            BoardListDTO dto = BoardListDTO.from(board, meta, status);;
            String nickname= memberRepository.findNicknameByUserid(board.getUserid());
            dto.setNickname(nickname);
            return dto;
        });
    }



    @Transactional
    @Override
    public BoardViewDTO getBoardById(Long id) {
        Board board = boardRepository.findById(id).orElseThrow();
        BoardMeta meta = boardMetaRepository.findById(board.getId()).orElseThrow();
        BoardStatus status = boardStatusRepository.findById(board.getId()).orElseThrow();
        BoardViewDTO dto = BoardViewDTO.from(board, meta, status);

        String nickname= memberRepository.findNicknameByUserid(board.getUserid());
        dto.setNickname(nickname);
        return dto;

    }


    @Transactional
    @Override
    public BoardDTO updateBoardById(BoardDTO dto, Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + id));

        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());

        Board updated = boardRepository.save(board);
        return BoardDTO.fromEntity(updated);
    }

    @Transactional
    @Override
    public void deleteBoardById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + id));
        boardRepository.delete(board);
    }

    @Transactional
    @Override
    public void createComment(CommentDTO dto) {
        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + dto.getBoardId()));

        Comment comment = dto.toEntity(board);
        commentRepository.save(comment);
    }


    @Transactional
    @Override
    public List<CommentDTO> getCommentsByBoardId(Long boardId) {
        List<Comment> comments = commentRepository.findAllByBoardId(boardId);
        List<CommentDTO> dtoList = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDTO commentDTO = CommentDTO.fromEntity(comment);
            if (commentDTO.getParentId() != null || commentDTO.getGrandParentId() != null) {
                if (commentDTO.getGrandParentId() == null) {
                    // 참조를 했는데 grandParent==null 이면 depth==1
                    commentDTO.setReferencedNickname(commentRepository.findNicknameById(commentDTO.getParentId()));
                    commentDTO.setReferencedContent(commentRepository.findContentById(commentDTO.getParentId()));
                } else {
                    // depth==2
                    commentDTO.setReferencedNickname(commentRepository.findNicknameById(commentDTO.getGrandParentId()));
                    commentDTO.setReferencedContent(commentRepository.findContentById(commentDTO.getGrandParentId()));
                }
            }
            dtoList.add(commentDTO);
        }
        return dtoList;
    }

    @Transactional
    @Override
    public void deleteComment(CommentUpdateDTO dto) {
        Comment comment = commentRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        comment.setNickname("****");
        comment.setContent("삭제된 댓글입니다");
        comment.setModifiedDate(LocalDateTime.now());

        commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void updateComment(CommentUpdateDTO dto) {
        Comment comment = commentRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("댓글 없음"));

        comment.setContent(dto.getContent());
        comment.setNickname(dto.getNickname());

    }

    @Transactional
    @Override
    public void incrementViewCount(Long boardId, HttpSession session) {
        // 세션에서 이미 조회한 게시글 목록을 가져옴
        List<Long> viewedBoardIds = (List<Long>) session.getAttribute("viewedBoards");

        if (viewedBoardIds == null) {
            viewedBoardIds = new ArrayList<>();
        }

        if (!viewedBoardIds.contains(boardId)) {
            // 해당 게시글의 조회수를 증가시킴
            BoardStatus boardStatus = boardStatusRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("게시판 상태를 찾을 수 없습니다."));
            boardStatus.setViewCount(boardStatus.getViewCount() + 1);
            boardStatusRepository.save(boardStatus);

            // 세션에 해당 게시글을 조회한 기록을 추가
            viewedBoardIds.add(boardId);
            session.setAttribute("viewedBoards", viewedBoardIds);
        }
    }

    // 게시글 좋아요 증가
    @Transactional
    public void increaseLike(Long boardId) {
        boardStatusRepository.increaseLike(boardId);
    }

    // 게시글 좋아요 감소
    @Transactional
    public void decreaseLike(Long boardId) {
        boardStatusRepository.decreaseLike(boardId);
    }

    // 게시글 싫어요 증가
    @Transactional
    public void increaseDisLike(Long boardId) {
        boardStatusRepository.increaseDisLike(boardId);
    }

    // 게시글 싫어요 감소
    @Transactional
    public void decreaseDisLike(Long boardId) {
        boardStatusRepository.decreaseDisLike(boardId);
    }

    // 댓글 좋아요 증가
    @Transactional
    public void increaseCommentLike(Long commentId) {
        commentRepository.increaseLike(commentId);
    }

    // 댓글 좋아요 감소
    @Transactional
    public void decreaseCommentLike(Long commentId) {
        commentRepository.decreaseLike(commentId);
    }

    // 댓글 싫어요 증가
    @Transactional
    public void increaseCommentDisLike(Long commentId) {
        commentRepository.increaseDisLike(commentId);
    }

    // 댓글 싫어요 감소
    @Transactional
    public void decreaseCommentDisLike(Long commentId) {
        commentRepository.decreaseDisLike(commentId);
    }

}
