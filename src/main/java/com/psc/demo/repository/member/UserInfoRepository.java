package com.psc.demo.repository.member;

import com.psc.demo.domain.member.Member;
import com.psc.demo.domain.member.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    boolean existsByEmail(String email);

    UserInfo findByMember(Member member);

}
