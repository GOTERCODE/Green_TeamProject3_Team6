package org.zerock.guestbook.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.guestbook.entity.Guestbook;

import java.util.List;

public interface GuestbookRepository extends JpaRepository<Guestbook, Long> {

    // 평점 기준으로 상위 3개 게임 조회
    @Query(value = "SELECT g FROM Guestbook g ORDER BY (g.scoreSum / NULLIF(g.scoreCount, 0)) DESC")
    List<Guestbook> findTopByOrderByScoreDesc(Pageable pageable);

    // 최신 등록순으로 상위 3개 게임 조회
    @Query(value = "SELECT g FROM Guestbook g ORDER BY g.date DESC")
    List<Guestbook> findTopByOrderByDateDesc(Pageable pageable);
}
