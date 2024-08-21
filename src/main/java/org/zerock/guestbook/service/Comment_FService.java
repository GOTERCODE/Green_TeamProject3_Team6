package org.zerock.guestbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.entity.Comment_F;
import org.zerock.guestbook.repository.Comment_FRepository;

import java.util.List;
import java.util.Optional;

@Service
public class Comment_FService {

    @Autowired
    private Comment_FRepository comment_fRepository;

    // 아래 필드 사용하지 않으므로 삭제
    // private BoardFreeService boardGameService;

    public void addComment(Comment_F comment_f) {
        comment_fRepository.save(comment_f); // 인스턴스 메서드 사용
    }

    public List<Comment_F> getCommentsByBoardFreeId(Long boardFreeId) {
        return comment_fRepository.findByBfcBfid(boardFreeId.toString()); // 인스턴스 메서드 사용
    }

    public void updateComment(Long commentId, String content) {
        Optional<Comment_F> optionalComment = comment_fRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment_F comment_f = optionalComment.get();
            comment_f.setBfcContent(content);
            comment_fRepository.save(comment_f);
        }
    }

    public void deleteComment(Long commentId) {
        comment_fRepository.deleteById(commentId);
    }
}
