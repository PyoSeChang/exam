package com.psc.demo.controller.member;

import com.psc.demo.dto.member.LoginRequestDTO;
import com.psc.demo.dto.member.LoginResult;
import com.psc.demo.dto.member.MemberDTO;
import com.psc.demo.service.member.MemberServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberServiceImpl memberService;

    @GetMapping("signup")
    public String displaySignup() {
        return "member/signup";
    }

    @PostMapping("signup")
    public String processSingup(@ModelAttribute("member") MemberDTO member) {
        memberService.registerMember(member);
        return "member/login";
    }

    @GetMapping("login")
    public String login() {
        return "member/login";
    }

    @PostMapping("login")
    public String processLogin(@ModelAttribute("loginRequest") LoginRequestDTO loginRequest, Model model, HttpSession session) {
        LoginResult loginResult= memberService.login(loginRequest);
        switch (loginResult) {
            case SUCCESS_USER:
                // 일반 사용자 로그인 성공
                session.setAttribute("loginMember", memberService.findByUsername(loginRequest.getUsername()));
                return "redirect:/board/main";

            case SUCCESS_ADMIN:
                // 관리자 로그인 성공
                session.setAttribute("loginMember", memberService.findByUsername(loginRequest.getUsername()));
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
}
