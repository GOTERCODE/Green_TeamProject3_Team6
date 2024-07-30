package org.zerock.guestbook.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.guestbook.entity.BoardGame;

public interface BoardGameService {
    Page<BoardGame> findAll(Pageable pageable);

    // 수정된 메서드 시그니처
    Page<BoardGame> searchByKeyword(String keyword, String sortOrder, Pageable pageable);

    BoardGame getBoardGameById(Long id);

    BoardGame createBoardGame(BoardGame boardGame);

    void deleteBoardGame(Long id);
}
