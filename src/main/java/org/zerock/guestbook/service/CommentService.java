package org.zerock.guestbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.entity.Comment;
import org.zerock.guestbook.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    private BoardGameService boardGameService;

    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }

    public List<Comment> getCommentsByBoardGameId(Long boardGameId) {
        return commentRepository.findByBgcBgid(boardGameId.toString());
    }
}
