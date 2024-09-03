package org.zerock.guestbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.guestbook.entity.Images;

public interface ImagesRepository extends JpaRepository<Images, Long> {
}
