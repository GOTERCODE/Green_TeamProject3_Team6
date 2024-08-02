package org.zerock.guestbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.guestbook.entity.Board_game;

public interface UserBoardRepository extends JpaRepository<Board_game, String> {
    Board_game findByUsername(String username);
}



