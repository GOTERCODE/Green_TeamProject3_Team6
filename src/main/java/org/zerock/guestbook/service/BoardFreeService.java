package org.zerock.guestbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.guestbook.entity.BoardFree;
import org.zerock.guestbook.entity.Member;
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
        return boardFreeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
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

    public Page<BoardFree> getAllBoardFree(Pageable pageable) {
        return boardFreeRepository.findAll(pageable);
    }

    public Page<BoardFree> searchByTitle(String keyword, Pageable pageable) {
        return boardFreeRepository.findByTitleContaining(keyword, pageable);
    }

    public Page<BoardFree> searchByContent(String keyword, Pageable pageable) {
        return boardFreeRepository.findByContentContaining(keyword, pageable);
    }

    public Page<BoardFree> searchByWriter(String keyword, Pageable pageable) {
        return boardFreeRepository.findByWriterContaining(keyword, pageable);
    }

    public List<BoardFree> getTop5BoardFree() {
        return boardFreeRepository.findTop5ByOrderByDateDesc();
    }


}
