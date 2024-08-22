package org.zerock.guestbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.entity.BoardFree;
import org.zerock.guestbook.entity.BoardFreeLike;
import org.zerock.guestbook.entity.BoardFreeLikeId;
import org.zerock.guestbook.repository.BoardFreeLikeRepository;
import org.zerock.guestbook.repository.BoardFreeRepository;

@Service
public class BoardFreeLikeService {

    @Autowired
    private BoardFreeLikeRepository boardFreeLikeRepository;

    @Autowired
    private BoardFreeRepository boardFreeRepository;

    public void toggleLike(Long memberId, Long boardFreeId) {
        BoardFree boardFree = boardFreeRepository.findById(boardFreeId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        BoardFreeLikeId boardFreeLikeId = new BoardFreeLikeId(memberId, boardFreeId);
        boolean hasLiked = boardFreeLikeRepository.existsById(boardFreeLikeId);

        if (hasLiked) {
            // 이미 추천한 경우 추천 취소
            decrementLikeCount(boardFreeId);
            boardFreeLikeRepository.deleteById(boardFreeLikeId);
        } else {
            // 추천하지 않은 경우 추천 추가
            incrementLikeCount(boardFreeId);
            boardFreeLikeRepository.save(new BoardFreeLike(memberId, boardFreeId));
        }
    }

    public Long countLikes(Long boardFreeId) {
        return boardFreeLikeRepository.countByBoardFreeId(boardFreeId);
    }

    private void incrementLikeCount(Long boardFreeId) {
        BoardFree boardFree = boardFreeRepository.findById(boardFreeId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        boardFree.setLike(boardFree.getLike() + 1);
        boardFreeRepository.save(boardFree);
    }

    private void decrementLikeCount(Long boardFreeId) {
        BoardFree boardFree = boardFreeRepository.findById(boardFreeId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        boardFree.setLike(Math.max(0, boardFree.getLike() - 1)); // 추천 수가 0보다 작아지지 않도록
        boardFreeRepository.save(boardFree);
    }
}
