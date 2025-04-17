package com.psc.demo.controller.member;

import com.psc.demo.domain.member.Member;
import com.psc.demo.domain.member.UserInfo;
import com.psc.demo.dto.member.LoginRequestDTO;
import com.psc.demo.dto.member.LoginResult;
import com.psc.demo.dto.member.MemberDTO;
import com.psc.demo.dto.member.UserInfoDTO;
import com.psc.demo.repository.member.MemberRepository;
import com.psc.demo.repository.member.UserInfoRepository;
import com.psc.demo.security.model.CustomUserDetails;
import com.psc.demo.service.member.MemberServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

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
    public String processLogin(@ModelAttribute("loginRequest") LoginRequestDTO loginRequest,
                               HttpServletRequest request,
                               Model model) {
        // 1. 사용자 조회 (DB)
        Member member = memberRepository.findByUserid(loginRequest.getUserid())
                .orElse(null);

        if (member == null) {
            model.addAttribute("error", "존재하지 않는 아이디입니다.");
            return "member/login";
        }

        // 2. 비밀번호 비교 (BCrypt 해시)
        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            model.addAttribute("error", "비밀번호가 틀렸습니다.");
            return "member/login";
        }

        // 3. CustomUserDetails 생성
        CustomUserDetails userDetails = new CustomUserDetails(member);

        // 4. Authentication 객체 생성
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,                     // principal
                null,                            // credentials
                userDetails.getAuthorities()     // 권한 목록
        );

        // 5. SecurityContext 생성 + 등록
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        // 6. 세션에 SecurityContext 저장
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", context);

        // 7. 로그인 성공 → 메인 페이지로 이동
        return "redirect:/board/main";
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
