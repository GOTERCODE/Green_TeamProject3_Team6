package org.zerock.guestbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.guestbook.entity.BoardGame;

@Repository
public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {
}
