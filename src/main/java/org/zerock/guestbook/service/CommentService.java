package org.zerock.guestbook.service;

import org.zerock.guestbook.entity.Comment;

import java.util.List;

public interface CommentService {
    void addComment(Comment comment);

    List<Comment> getCommentsByBoardGameId(Long boardGameId);

    void updateComment(Long commentId, String content);

    void deleteComment(Long commentId);

    void deleteCommentsByBoardGameId(Long boardGameId);  // 인터페이스에서 정의
}
