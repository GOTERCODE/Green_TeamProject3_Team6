// BoardGameService.java
package org.zerock.guestbook.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.zerock.guestbook.entity.BoardFree;
import org.zerock.guestbook.entity.BoardGame;

import java.util.List;

public interface BoardGameService {
    Page<BoardGame> findAll(Pageable pageable);

    Page<BoardGame> searchByKeywordAndTags(String keyword, String[] tags, String sortOrder, Pageable pageable);

    BoardGame getBoardGameById(Long id);

    BoardGame createBoardGame(BoardGame boardGame);

    BoardGame updateBoardGame(BoardGame boardGame);

    void deleteBoardGame(Long id);

    void updateBoardGameScore(Long boardGameId, Double newScore, boolean isNew);


}
