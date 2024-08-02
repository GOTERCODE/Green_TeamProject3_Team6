package org.zerock.guestbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.guestbook.entity.BoardGame;

public interface UserBoardGameRepository extends JpaRepository<BoardGame, String> {
        BoardGame findByWriter(String writer);
}
