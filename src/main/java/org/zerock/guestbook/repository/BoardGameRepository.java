package org.zerock.guestbook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.guestbook.entity.BoardGame;

public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {

    @Query("SELECT bg FROM BoardGame bg " +
            "WHERE LOWER(bg.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY " +
            "CASE WHEN :sortOrder = 'scoreDesc' THEN (bg.scoreSum / bg.scoreCount) ELSE NULL END DESC, " +
            "CASE WHEN :sortOrder = 'scoreAsc' THEN (bg.scoreSum / bg.scoreCount) ELSE NULL END ASC, " +
            "CASE WHEN :sortOrder = 'dateAsc' THEN bg.date ELSE NULL END ASC, " +
            "CASE WHEN :sortOrder = 'dateDesc' THEN bg.date ELSE NULL END DESC")
    Page<BoardGame> findByTitleContainingIgnoreCase(@Param("keyword") String keyword,
                                                    @Param("sortOrder") String sortOrder,
                                                    Pageable pageable);
}
