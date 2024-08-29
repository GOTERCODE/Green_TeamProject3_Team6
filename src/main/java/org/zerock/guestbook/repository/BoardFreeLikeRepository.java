package org.zerock.guestbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.guestbook.entity.BoardFreeLike;
import org.zerock.guestbook.entity.BoardFreeLikeId;

public interface BoardFreeLikeRepository extends JpaRepository<BoardFreeLike, BoardFreeLikeId> {
    Long countByBoardIdx(Long boardIdx);  // 게시글에 대한 총 추천 수

    // 특정 회원과 게시글에 대한 추천 존재 여부를 확인하는 메서드
    boolean existsById(BoardFreeLikeId id);

    // 특정 회원과 게시글에 대한 추천 삭제
    void deleteById(BoardFreeLikeId id);

    Long countByBoardFreeId(Long boardFreeId);


    // 특정 사용자가 특정 게시글에 대해 추천했는지 여부를 확인하는 메서드
    boolean existsByMemberNumAndBoardIdx(Long memberNum, Long boardIdx);

}
