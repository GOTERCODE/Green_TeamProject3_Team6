package org.zerock.guestbook.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.guestbook.entity.Member;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, String> {
    Member findByUsername(String writer);

    @Query("SELECT m FROM Member m WHERE LOWER(m.nickname) = LOWER(:username)")
    Member findByUsername2(@Param("username") String username);

    @Modifying
    @Query("UPDATE BoardGameTest SET writer = LOWER(:newName) WHERE writer = LOWER(:oldName)")
    void update_boardfree(@Param("oldName") String oldName, @Param("newName") String newName);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.mute = true WHERE m.nickname = LOWER(:username)")
    void mute(@Param("username") String username);

    @Query("SELECT m FROM Member m WHERE LOWER(m.nickname) = LOWER(:username)")
    Member findByUsername3(@Param("username") String username);


}
