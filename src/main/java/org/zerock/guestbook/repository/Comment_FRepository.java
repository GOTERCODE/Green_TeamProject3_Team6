package org.zerock.guestbook.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.zerock.guestbook.entity.Comment_F;

import java.util.List;

@Repository
public interface Comment_FRepository extends JpaRepository<Comment_F, Long> {

    List<Comment_F> findByBfcBfid(String boardFreeId);

    @Modifying
    @Query("DELETE FROM Comment_F c WHERE c.bfcBfid = :bfcBfid")
    void deleteBybfcBfid(@Param("bfcBfid") String bfcBfid);
}
