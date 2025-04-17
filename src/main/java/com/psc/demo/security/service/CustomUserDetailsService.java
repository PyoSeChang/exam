package com.psc.demo.security.service;

import com.psc.demo.domain.member.Member;
import com.psc.demo.repository.member.MemberRepository;
import com.psc.demo.security.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username은 보통 userid나 email임 → 로그인 폼에서 받는 값
        Member member = memberRepository.findByUserid(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("존재하지 않는 사용자입니다: " + username));

        // 찾은 member를 UserDetails(=CustomUserDetails)로 래핑해서 반환
        return new CustomUserDetails(member);
    }
}
