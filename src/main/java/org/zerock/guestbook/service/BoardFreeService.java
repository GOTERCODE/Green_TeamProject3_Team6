package org.zerock.guestbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.entity.BoardFree;
import org.zerock.guestbook.repository.BoardFreeRepository;

import java.util.List;

@Service
public class BoardFreeService {

    @Autowired
    private BoardFreeRepository boardFreeRepository;

    public List<BoardFree> getAllBoardFree() {
        return boardFreeRepository.findAll();
    }

    public BoardFree getBoardFreeById(Long id) {
        return boardFreeRepository.findById(id).orElse(null);
    }

    public void createBoardFree(BoardFree boardFree) {
        boardFreeRepository.save(boardFree);
    }

    public void updateBoardFree(BoardFree boardFree) {
        boardFreeRepository.save(boardFree);
    }

    public void deleteBoardFree(Long id) {
        boardFreeRepository.deleteById(id);
    }
}
