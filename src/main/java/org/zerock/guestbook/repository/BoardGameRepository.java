package org.zerock.guestbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.zerock.guestbook.entity.BoardGame;

public interface BoardGameRepository extends JpaRepository<BoardGame, Long>, JpaSpecificationExecutor<BoardGame> {
}
