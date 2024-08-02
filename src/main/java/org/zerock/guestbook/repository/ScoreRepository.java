package org.zerock.guestbook.repository;

import org.zerock.guestbook.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.guestbook.entity.ScoreId;

public interface ScoreRepository extends JpaRepository<Score, ScoreId> {
}
