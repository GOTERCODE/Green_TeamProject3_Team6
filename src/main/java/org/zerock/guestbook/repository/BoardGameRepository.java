package org.zerock.guestbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.guestbook.entity.BoardGame;

public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {
    // JpaRepository는 기본적으로 findAll(Pageable pageable) 메서드를 제공하여 페이징 처리를 지원합니다.
}
