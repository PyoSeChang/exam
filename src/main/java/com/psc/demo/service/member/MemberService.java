package com.psc.demo.service.member;

import com.psc.demo.domain.member.UserInfo;
import com.psc.demo.dto.member.LoginRequestDTO;
import com.psc.demo.dto.member.LoginResult;
import com.psc.demo.dto.member.MemberDTO;
import com.psc.demo.dto.member.UserInfoDTO;

public interface MemberService {
    // 가입하기
    void registerMember(MemberDTO mDTO, UserInfoDTO uDTO);
    // 탈퇴하기
    void withdrawMember(long memberId);
    // 로그인
    LoginResult login(LoginRequestDTO dto);
    // 아이디 중복체크
    boolean checkDuplicatedUserId(String username);
    // 닉네임 중복체크
    boolean checkDuplicatedNickname(String nickname);

    //
    void updateUserInfo(UserInfoDTO dto, Long id);
}
