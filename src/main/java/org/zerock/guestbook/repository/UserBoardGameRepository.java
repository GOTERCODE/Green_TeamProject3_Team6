package org.zerock.guestbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.guestbook.entity.BoardGame;
import org.zerock.guestbook.entity.BoardGameTest;

import java.util.List;

public interface UserBoardGameRepository extends JpaRepository<BoardGameTest, String> {

        @Query("SELECT b FROM BoardGameTest b WHERE LOWER(b.writer) = LOWER(:writer)")
        List<BoardGameTest> findByWriterIgnoreCase(@Param("writer") String writer);


}
