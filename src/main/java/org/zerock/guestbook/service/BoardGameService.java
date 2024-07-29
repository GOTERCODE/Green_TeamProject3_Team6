package org.zerock.guestbook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.entity.BoardGame;
import org.zerock.guestbook.repository.BoardGameRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardGameService {

    private final BoardGameRepository boardGameRepository;

    public List<BoardGame> getAllBoardGames() {
        return boardGameRepository.findAll();
    }

    public BoardGame getBoardGameById(Long id) {
        return boardGameRepository.findById(id).orElse(null);
    }

    public BoardGame createBoardGame(BoardGame boardGame) {
        return boardGameRepository.save(boardGame);
    }

    public void deleteBoardGame(Long id) {
        boardGameRepository.deleteById(id);
    }
}
