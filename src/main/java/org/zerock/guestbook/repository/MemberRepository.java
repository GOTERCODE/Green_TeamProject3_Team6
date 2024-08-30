package org.zerock.guestbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.guestbook.entity.Member;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, String> {
    Member findByUsername(String writer);

    @Query("SELECT m FROM Member m WHERE LOWER(m.nickname) = LOWER(:username)")
    Member findByUsername2(@Param("username") String username);
}
