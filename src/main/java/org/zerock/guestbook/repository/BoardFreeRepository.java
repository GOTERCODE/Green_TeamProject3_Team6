package org.zerock.guestbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.guestbook.entity.BoardFree;

public interface BoardFreeRepository extends JpaRepository<BoardFree, Long> {
}
