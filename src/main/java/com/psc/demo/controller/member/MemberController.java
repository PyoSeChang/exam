package com.psc.demo.controller.member;

import com.psc.demo.domain.member.Member;
import com.psc.demo.domain.member.UserInfo;
import com.psc.demo.dto.member.LoginRequestDTO;
import com.psc.demo.dto.member.LoginResult;
import com.psc.demo.dto.member.MemberDTO;
import com.psc.demo.dto.member.UserInfoDTO;
import com.psc.demo.repository.member.MemberRepository;
import com.psc.demo.repository.member.UserInfoRepository;
import com.psc.demo.service.member.MemberServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberServiceImpl memberService;
    private final MemberRepository memberRepository;
    private final UserInfoRepository userInfoRepository;

    @GetMapping("signup")
    public String displaySignup() {
        return "member/signup";
    }

    @PostMapping("signup")
    public String processSingup(@ModelAttribute("member") MemberDTO member, @ModelAttribute("userinfo") UserInfoDTO userinfo) {
        memberService.registerMember(member, userinfo);
        return "member/login";
    }

    @GetMapping("/checkUserId")
    public ResponseEntity<Map<String, Boolean>> checkUserId(@RequestParam String userid) {
        boolean exists = memberRepository.existsByUserid(userid);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/checkNickname")
    public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestParam String nickname) {
        boolean exists = memberRepository.existsByNickname(nickname);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/checkEmail")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean exists = userInfoRepository.existsByEmail(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("login")
    public String login() {
        return "member/login";
    }

    @PostMapping("login")
    public String processLogin(@ModelAttribute("loginRequest") LoginRequestDTO loginRequest, Model model, HttpSession session,
                               HttpServletRequest request) {
        LoginResult loginResult= memberService.login(loginRequest);
        switch (loginResult) {
            case SUCCESS_USER:
                // 일반 사용자 로그인 성공
                if (session != null) {
                    session.invalidate();  // 이전 세션 무효화
                }
                session = request.getSession(true);
                session.setAttribute("loginMember", memberService.findByUserid(loginRequest.getUserid()));
                System.out.println("로그인 세션: "+session.getAttribute("loginMember"));
                return "redirect:/board/main";

            case SUCCESS_ADMIN:
                // 관리자 로그인 성공
                if (session != null) {
                    session.invalidate();  // 이전 세션 무효화
                }
                session = request.getSession(true);
                session.setAttribute("loginMember", memberService.findByUserid(loginRequest.getUserid()));
                return "redirect:/board/main";

            case FAIL_ID_NOT_FOUND:
                model.addAttribute("error", "존재하지 않는 아이디입니다.");
                return "member/login";

            case FAIL_PASSWORD_INCORRECT:
                model.addAttribute("error", "비밀번호가 틀렸습니다.");
                return "member/login";

            default:
                model.addAttribute("error", "알 수 없는 오류가 발생했습니다.");
                return "member/login";
        }
    }

    @PostMapping("/logout")
    public String logoutUser(HttpSession session) {
        // 세션 초기화
        session.invalidate();  // 로그아웃 시 세션을 무효화하여 모든 세션 데이터 삭제

        return "redirect:/member/login";  // 로그아웃 후 로그인 페이지로 리다이렉트
    }

    @GetMapping("editUserInfo")
    public String displayEditUserInfo(HttpSession session, Model model) throws AccessDeniedException {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember != null) {
            UserInfo userInfo = userInfoRepository.findByMember(loginMember);
            UserInfoDTO dto = UserInfoDTO.fromEntity(userInfo);
            model.addAttribute("userInfoDTO", dto);
        } else {
            throw new AccessDeniedException("로그인이 필요한 서비스입니다");
        }
        return "member/mypage";
    }

    @PostMapping("editUserInfo")
    public String editUserInfo(@ModelAttribute("userInfoDTO") UserInfoDTO userInfoDTO,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) throws AccessDeniedException {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            throw new AccessDeniedException("로그인이 필요한 서비스입니다.");
        }
        Long id=loginMember.getId();
        memberService.updateUserInfo(userInfoDTO, id);
        redirectAttributes.addFlashAttribute("message", "정보가 성공적으로 수정되었습니다!");
        return "redirect:/board/main";
    }
}
