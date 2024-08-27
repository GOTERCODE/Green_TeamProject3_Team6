package org.zerock.guestbook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.guestbook.entity.News;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {

    Page<News> findByTitleContaining(String keyword, Pageable pageable);

    @Query("SELECT n FROM News n WHERE n.category LIKE %:category% AND n.title LIKE %:keyword%")
    Page<News> findByCategoryAndTitleContaining(@Param("category") String category, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT n FROM News n WHERE n.id = :id")
    News NewsPage_open(@Param("id") Long id);

    @Query("SELECT n FROM News n WHERE n.id = :id")
    News serach_news(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE News n SET n.title = :title, n.content = :content, n.thumbnail = :thumbnail, n.comment = :comment, n.category = :category WHERE n.id = :id")
    void update_news(@Param("id") Long id,
                    @Param("title") String title,
                    @Param("content") String content,
                    @Param("thumbnail") String thumbnail,
                    @Param("comment") String comment,
                    @Param("category") String category);

    Optional<News> findById(Long id);

    List<News> findTop4ByOrderByDateDesc();
}
