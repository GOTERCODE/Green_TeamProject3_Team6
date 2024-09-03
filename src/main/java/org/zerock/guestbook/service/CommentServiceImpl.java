package org.zerock.guestbook.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.guestbook.entity.Comment;
import org.zerock.guestbook.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsByBoardGameId(Long boardGameId) {
        return commentRepository.findByBgcBgid(boardGameId.toString());
    }

    @Override
    public void updateComment(Long commentId, String content) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setBgcContent(content);
            commentRepository.save(comment);
        }
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public void deleteCommentsByBoardGameId(Long boardGameId) {
        commentRepository.deleteByBgcBgid(boardGameId.toString());
    }
}
