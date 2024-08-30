package org.zerock.guestbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.guestbook.entity.BoardGameTest;
import org.zerock.guestbook.entity.Comment_F;

import java.util.List;

public interface UserBoardGameRepository extends JpaRepository<BoardGameTest, String> {

        @Query("SELECT b FROM BoardGameTest b WHERE LOWER(b.writer) = LOWER(:writer)")
        List<BoardGameTest> findByWriterIgnoreCase(@Param("writer") String writer);

        @Query("SELECT b FROM Comment_F b WHERE LOWER(b.bfcWriter) = LOWER(:writer)")
        List<Comment_F> findByWriterIgnoreCase2(@Param("writer") String writer);

}
