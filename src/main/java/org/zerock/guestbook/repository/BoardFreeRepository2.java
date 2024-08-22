package org.zerock.guestbook.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zerock.guestbook.entity.BoardFree;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BoardFreeRepository2 extends CrudRepository<BoardFree, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE BoardFree bf SET bf.writer = :newNick WHERE bf.writer = :oldNick")
    void updateWriter(@Param("oldNick") String oldNick, @Param("newNick") String newNick);
}

