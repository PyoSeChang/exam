package com.psc.demo.repository.member;

import com.psc.demo.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 로그인 ID로 회원 조회
    Optional<Member> findByUserid(String userid);

    // 사용자 ID 중복 체크
    boolean existsByUserid(String userid);

    // 닉네임 중복 체크
    boolean existsByNickname(String nickname);

    @Query("SELECT m.nickname FROM Member m WHERE m.userid = :userid")
    String findNicknameByUserid(@Param("userid") String userid);

    @Query("SELECT m.userid FROM Member m WHERE m.nickname = :nickname")
    String findUseridByNickname(String nickname);
}
