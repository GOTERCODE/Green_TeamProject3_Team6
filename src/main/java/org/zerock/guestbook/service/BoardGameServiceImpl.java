package org.zerock.guestbook.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.guestbook.entity.BoardGame;
import org.zerock.guestbook.repository.BoardGameRepository;
import org.zerock.guestbook.repository.BoardGameSpecifications;

@Service
public class BoardGameServiceImpl implements BoardGameService {

    private final BoardGameRepository boardGameRepository;

    public BoardGameServiceImpl(BoardGameRepository boardGameRepository) {
        this.boardGameRepository = boardGameRepository;
    }

    @Override
    public Page<BoardGame> findAll(Pageable pageable) {
        return boardGameRepository.findAll(pageable);
    }

    @Override
    public Page<BoardGame> searchByKeywordAndTags(String keyword, String[] tags, String sortOrder, Pageable pageable) {
        Specification<BoardGame> spec = Specification.where(BoardGameSpecifications.hasKeyword(keyword))
                .and(BoardGameSpecifications.hasTags(tags))
                .and(BoardGameSpecifications.sortBy(sortOrder));
        return boardGameRepository.findAll(spec, pageable);
    }

    @Override
    public BoardGame getBoardGameById(Long id) {
        return boardGameRepository.findById(id).orElse(null);
    }

    @Override
    public BoardGame createBoardGame(BoardGame boardGame) {
        return boardGameRepository.save(boardGame);
    }

    @Override
    public void deleteBoardGame(Long id) {
        boardGameRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void updateBoardGameScore(Long boardGameId, Double newScore, boolean isNew) {
        BoardGame boardGame = boardGameRepository.findById(boardGameId)
                .orElseThrow(() -> new RuntimeException("BoardGame not found"));

        if (isNew) {
            boardGame.addScore(newScore);
        } else {
            boardGame.removeScore(newScore);
        }

        boardGameRepository.save(boardGame);
    }
}
