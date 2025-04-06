package com.psc.demo.service.member;

import com.psc.demo.domain.member.Member;
import com.psc.demo.domain.member.UserInfo;
import com.psc.demo.dto.member.LoginRequestDTO;
import com.psc.demo.dto.member.LoginResult;
import com.psc.demo.dto.member.MemberDTO;
import com.psc.demo.dto.member.UserInfoDTO;
import com.psc.demo.repository.member.MemberRepository;
import com.psc.demo.repository.member.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final UserInfoRepository userInfoRepository;

    @Transactional
    @Override
    public void registerMember(MemberDTO mDTO, UserInfoDTO uDTO) {
        Member member = toEntity(mDTO);
        memberRepository.save(member);
        UserInfo userInfo = uDTO.toEntity();
        userInfo.setMember(member);
        userInfoRepository.save(userInfo);

    }

    @Override
    public void withdrawMember(long memberId) {
        memberRepository.deleteById(memberId);
    }

    @Override
    public LoginResult login(LoginRequestDTO dto) {
        return memberRepository.findByUserid(dto.getUserid())
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
    public boolean checkDuplicatedUserId(String userid) {
        return memberRepository.existsByUserid(userid);
    }

    @Override
    public boolean checkDuplicatedNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Transactional
    @Override
    public void updateUserInfo(UserInfoDTO dto, Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        UserInfo userInfo = userInfoRepository.findByMember(member);

        userInfo.setName(dto.getName());
        userInfo.setGender(dto.getGender());
        userInfo.setBirthDate(dto.getBirthDate());
        userInfo.setEmail(dto.getEmail());
        userInfo.setTel(dto.getTel());

    }

    private Member toEntity(MemberDTO dto) {
        return Member.builder()
                .userid(dto.getUserid())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .role(dto.getRole() != null ? dto.getRole() : Member.Role.USER) // 불필요한 조건문 제거
                .build();
    }

    public Member findByUserid(String userid) {
        return memberRepository.findByUserid(userid)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
    }

}
