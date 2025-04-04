package com.psc.demo.dto.member;

import com.psc.demo.domain.member.Member.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberDTO {

    private final String username;
    private final String password;
    private final String nickname;
    private final Role role; // 기본값은 보통 USER로 설정
}
