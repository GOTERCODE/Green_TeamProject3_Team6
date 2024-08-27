package org.zerock.guestbook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.guestbook.entity.BoardFree;

import java.util.List;

public interface BoardFreeRepository extends JpaRepository<BoardFree, Long> {

    Page<BoardFree> findByTitleContaining(String title, Pageable pageable);

    Page<BoardFree> findByContentContaining(String content, Pageable pageable);

    Page<BoardFree> findByWriterContaining(String writer, Pageable pageable);

    List<BoardFree> findTop5ByOrderByDateDesc();
}
