package org.zerock.guestbook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.guestbook.entity.News;
import org.springframework.data.repository.query.Param;

public interface NewsRepository extends JpaRepository<News, Long> {

    Page<News> findByTitleContaining(String keyword, Pageable pageable);

    @Query("SELECT n FROM News n WHERE n.category LIKE %:category% AND n.title LIKE %:keyword%")
    Page<News> findByCategoryAndTitleContaining(@Param("category") String category, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT n FROM News n WHERE n.id = :id")
    News NewsPage_open(@Param("id") Long id);

}
