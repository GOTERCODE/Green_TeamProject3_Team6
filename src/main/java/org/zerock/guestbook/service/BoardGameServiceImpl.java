package org.zerock.guestbook.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.entity.BoardGame;
import org.zerock.guestbook.repository.BoardGameRepository;

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
    public Page<BoardGame> searchByKeyword(String keyword, String sortOrder, Pageable pageable) {
        return boardGameRepository.findByTitleContainingIgnoreCase(keyword, sortOrder, pageable);
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
}
