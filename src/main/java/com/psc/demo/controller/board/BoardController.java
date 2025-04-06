package com.psc.demo.controller.board;

import com.psc.demo.domain.board.Board;
import com.psc.demo.domain.member.Member;
import com.psc.demo.dto.board.*;
import com.psc.demo.dto.member.MemberDTO;
import com.psc.demo.repository.board.CommentRepository;
import com.psc.demo.service.board.BoardService;
import com.psc.demo.service.board.BoardServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardServiceImpl boardService;
    private final CommentRepository commentRepository;

    @GetMapping("main")
    public String displayMainPage() {
        return "board/main";
    }

    @GetMapping("insert")
    public String displayInsertPage(Model model) {
        model.addAttribute("board", new BoardViewDTO());
        return "board/insert";
    }

    @PostMapping("insert")
    public String insertBoard(BoardViewDTO dto) {
        boardService.createBoard(dto);
        return "redirect:/board/main";
    }

    @GetMapping("list")
    public String displayBoardList(
            @RequestParam(value = "category", required = false) Board.Category category,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "searchType", required = false, defaultValue = "title") String searchType,
            @PageableDefault(size = 10) Pageable pageable,
            Model model) {
        System.out.println("searchType: "+searchType);
        System.out.println("keyword: "+keyword);
        System.out.println("searchType: "+searchType);
        System.out.println("keyword: "+keyword);
        System.out.println("category: "+category);
        Page<BoardListDTO> boardList = boardService.getAllBoards(category, keyword, searchType, pageable);

        model.addAttribute("boardList", boardList); // Page<BoardDTO>
        model.addAttribute("categoryList", List.of("공지", "자유", "QnA", "토론"));
        model.addAttribute("selectedCategory", category);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType);

        return "board/list";
    }


    @GetMapping("detail")
    public String displayBoardDetail(Model model, @RequestParam(name="id") Long id, HttpSession session) {
        boardService.incrementViewCount(id, session);
        model.addAttribute("board", boardService.getBoardById(id));
        List<CommentDTO> comments = boardService.getCommentsByBoardId(id);
        model.addAttribute("commentList", comments);
        model.addAttribute("commentDTO", new CommentDTO());
        Member member = (Member) session.getAttribute("loginMember");
        model.addAttribute("loginNickname", member != null ? member.getNickname() : "");

        return "board/detail";
    }

    @GetMapping("update")
    public String displayBoardUpdate(@RequestParam(name="id") Long id, Model model) {
        BoardViewDTO boardviewDTO = boardService.getBoardById(id);
        model.addAttribute("board", boardviewDTO);
        return "board/update";
    }

    @PostMapping("update")
    public String updateBoard(BoardViewDTO dto,
                              @RequestParam(name = "id") Long id,
                              HttpSession session) throws AccessDeniedException {
        Member member = (Member) session.getAttribute("loginMember");
        String loginUserId = member.getUserid();
        String requestUserId = boardService.getBoardById(id).getUserid();

//        System.out.println("requestUserId="+requestUserId);
//        System.out.println("requestUserId="+requestUserId);

        if (!loginUserId.equals(requestUserId)) {
            // 권한 없음 또는 접근 거부 처리
            throw new AccessDeniedException("본인의 게시글만 수정 또는 삭제할 수 있습니다.");
        }

        boardService.updateBoardById(dto.toBoardDTO(), id);
        return "redirect:/board/detail?id=" + id;
    }


    @GetMapping("delete")
    public String deleteBoard(@RequestParam(name = "id") Long id,
                              HttpSession session) throws AccessDeniedException {
        Member member = (Member) session.getAttribute("loginMember");
        String loginUserId = member.getUserid();

        String requestUserId = boardService.getBoardById(id).getUserid();
        if (!loginUserId.equals(requestUserId)) {
            // 권한 없음 또는 접근 거부 처리
            throw new AccessDeniedException("본인의 게시글만 수정 또는 삭제할 수 있습니다.");
        }

        boardService.deleteBoardById(id);
        return "redirect:/board/list";
    }

    @PostMapping("insertComment")
    public String insertComment(@RequestParam(name= "boardId") Long boardId,
                                CommentDTO dto,
                                HttpSession session) throws AccessDeniedException {
        Member member = (Member) session.getAttribute("loginMember");
        if (member == null) {
            throw new AccessDeniedException("로그인 후 이용하실 수 있습니다.");
        }

        // 세션에서 nickname 설정
        dto.setNickname(member.getNickname());
        dto.setBoardId(boardId); // 혹시 form에서 boardId 안 넘겼을 경우 대비

        boardService.createComment(dto);
        return "redirect:/board/detail?id=" + boardId;
    }

    @PostMapping("updateComment")
    public String updateComment(CommentUpdateDTO dto, Long id,
                                HttpSession session) throws AccessDeniedException {
        Member member = (Member) session.getAttribute("loginMember");
        String loginUserNickname = member.getNickname();
        if (!loginUserNickname.equals(dto.getNickname())) {
            throw new AccessDeniedException("본인의 게시글만 수정 또는 삭제할 수 있습니다.");
        }
        boardService.updateComment(dto);
        Board board = commentRepository.findBoardIdById(dto.getId());
        Long boardId = board.getId();
        return "redirect:/board/detail?id=" + boardId;
    }

    @PostMapping("deleteComment")
    public String deleteComment(CommentUpdateDTO dto,
                                HttpSession session) throws AccessDeniedException {
        Member member = (Member) session.getAttribute("loginMember");
        String loginUserNickname = member.getNickname();
        System.out.println("loginUserNickname: " + loginUserNickname);
        System.out.println("loginUserNickname: " + loginUserNickname);
        System.out.println("getNickname: " + dto.getNickname());
        System.out.println("getNickname: " + dto.getNickname());
        if (!loginUserNickname.equals(dto.getNickname())) {
            throw new AccessDeniedException("본인의 게시글만 수정 또는 삭제할 수 있습니다.");
        }
        boardService.deleteComment(dto);
        Board board = commentRepository.findBoardIdById(dto.getId());
        Long boardId = board.getId();
        return "redirect:/board/detail?id=" + boardId;
    }

    @PostMapping("reactBoard")
    public String reactBoard(@RequestParam(name="boardId") Long boardId,
                             @RequestParam(name="reactType") String reactType,
                             HttpSession session) {

        // 세션에서 boardReaction 가져오기
        Map<Long, String> boardReaction = (Map<Long, String>) session.getAttribute("boardReaction");

        // boardReaction이 null이면 새로운 Map 생성
        if (boardReaction == null) {
            boardReaction = new HashMap<>();
            session.setAttribute("boardReaction", boardReaction);
        }

        // 현재 반응 가져오기
        String exReactType = boardReaction.get(boardId);

        // 반응이 없다면 처음으로 반응을 등록
        if (exReactType == null) {
            switch (reactType) {
                case "like":
                    boardService.increaseLike(boardId);
                    break;
                case "dislike":
                    boardService.increaseDisLike(boardId);
                    break;
                default:
                    break;
            }
            // 반응 등록 후 세션에 저장
            boardReaction.put(boardId, reactType);

        } else {
            // 이미 반응이 있을 경우, 반응을 변경
            if (exReactType.equals(reactType)) {
                // 같은 반응을 다시 누른 경우, 아무것도 하지 않음
                return "redirect:/board/detail?id=" + boardId;
            } else {
                // 반응을 변경 (좋아요 <-> 싫어요)
                switch (reactType) {
                    case "like":
                        boardService.increaseLike(boardId);
                        boardService.decreaseDisLike(boardId);
                        break;
                    case "dislike":
                        boardService.increaseDisLike(boardId);
                        boardService.decreaseLike(boardId);
                        break;
                    default:
                        break;
                }
                // 반응 변경 후 세션에 업데이트
                boardReaction.put(boardId, reactType);
            }
        }

        // 반응 처리 후 게시글 상세페이지로 리다이렉트
        return "redirect:/board/detail?id=" + boardId;
    }


    @PostMapping("reactComment")
    public String reactComment(@RequestParam(name="commentId") Long commentId,
                               @RequestParam(name="reactType") String reactType,
                               HttpSession session) {

        // 세션에서 commentReaction 가져오기
        Map<Long, String> commentReaction = (Map<Long, String>) session.getAttribute("commentReaction");

        // commentReaction이 null이면 새로운 Map 생성
        if (commentReaction == null) {
            commentReaction = new HashMap<>();
            session.setAttribute("commentReaction", commentReaction);
        }

        // 현재 반응 가져오기
        String exReactType = commentReaction.get(commentId);

        // 반응이 없다면 처음으로 반응을 등록
        if (exReactType == null) {
            switch (reactType) {
                case "like":
                    boardService.increaseCommentLike(commentId);  // 댓글 좋아요 증가
                    break;
                case "dislike":
                    boardService.increaseCommentDisLike(commentId);  // 댓글 싫어요 증가
                    break;
                default:
                    break;
            }
            // 반응 등록 후 세션에 저장
            commentReaction.put(commentId, reactType);

        } else {
            // 이미 반응이 있을 경우, 반응을 변경
            if (exReactType.equals(reactType)) {
                // 같은 반응을 다시 누른 경우, 아무것도 하지 않음
                return "redirect:/board/detail?id=" + commentId;
            } else {
                // 반응을 변경 (좋아요 <-> 싫어요)
                switch (reactType) {
                    case "like":
                        boardService.increaseCommentLike(commentId);
                        boardService.decreaseCommentDisLike(commentId);
                        break;
                    case "dislike":
                        boardService.increaseCommentDisLike(commentId);
                        boardService.decreaseCommentLike(commentId);
                        break;
                    default:
                        break;
                }
                // 반응 변경 후 세션에 업데이트
                commentReaction.put(commentId, reactType);
            }
        }

        // 반응 처리 후 게시글 상세페이지로 리다이렉트
        return "redirect:/board/detail?id=" + commentId;
    }



}
