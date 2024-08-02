package org.zerock.guestbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.guestbook.entity.Member;

import java.util.Optional;

public interface MemberRepository_Register extends JpaRepository<Member, String> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByUsername(String username);
}