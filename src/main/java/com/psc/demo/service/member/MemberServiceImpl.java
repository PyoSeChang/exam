package com.psc.demo.service.member;

import com.psc.demo.domain.member.Member;
import com.psc.demo.dto.member.LoginRequestDTO;
import com.psc.demo.dto.member.LoginResult;
import com.psc.demo.dto.member.MemberDTO;
import com.psc.demo.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public void registerMember(MemberDTO dto) {
        Member member = toEntity(dto);
        memberRepository.save(member);
    }

    @Override
    public void withdrawMember(long memberId) {
        memberRepository.deleteById(memberId);
    }

    @Override
    public LoginResult login(LoginRequestDTO dto) {
        return memberRepository.findByUsername(dto.getUsername())
                .map(member -> {
                    if (member.getPassword().equals(dto.getPassword())) {
                        return member.getRole() == Member.Role.ADMIN ?
                                LoginResult.SUCCESS_ADMIN : LoginResult.SUCCESS_USER;
                    } else {
                        return LoginResult.FAIL_PASSWORD_INCORRECT;
                    }
                })
                .orElse(LoginResult.FAIL_ID_NOT_FOUND);
    }

    @Override
    public boolean checkDuplicatedUserId(String username) {
        return memberRepository.existsByUsername(username);
    }

    @Override
    public boolean checkDuplicatedNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    private Member toEntity(MemberDTO dto) {
        return Member.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .role(dto.getRole() != null ? dto.getRole() : Member.Role.USER) // 불필요한 조건문 제거
                .build();
    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
    }

}
