package com.psc.demo.repository.member;

import com.psc.demo.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 로그인 ID로 회원 조회
    Optional<Member> findByUsername(String username);

    // 사용자 ID 중복 체크
    boolean existsByUsername(String username);

    // 닉네임 중복 체크
    boolean existsByNickname(String nickname);

}
