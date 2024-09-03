package org.zerock.guestbook.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.guestbook.entity.Comment_F;

@Repository
public interface BfCommentRepository extends CrudRepository<Comment_F, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Comment_F bc SET bc.bfcWriter = :newNick WHERE bc.bfcWriter = :oldNick" )
    void updateWriter2(@Param("oldNick" ) String oldNick, @Param("newNick" ) String newNick);
}
