package org.zerock.guestbook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.guestbook.entity.News;

public interface NewsRepository extends JpaRepository<News, Long> {

    Page<News> findByTitleContaining(String keyword, Pageable pageable);

    Page<News> findByCategoryAndTitleContaining(String category, String keyword, Pageable pageable);
}
