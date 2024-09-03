package org.zerock.guestbook.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.guestbook.entity.BoardGame;
import org.zerock.guestbook.entity.Comment;
import org.zerock.guestbook.entity.Member;
import org.zerock.guestbook.entity.Score;
import org.zerock.guestbook.service.BoardGameService;
import org.zerock.guestbook.service.CommentService;
import org.zerock.guestbook.service.ScoreService;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class BoardGameController {

    private final ScoreService scoreService;
    private final BoardGameService boardGameService;
    private final CommentService commentService;

    public BoardGameController(BoardGameService boardGameService, ScoreService scoreService, CommentService commentService) {
        this.boardGameService = boardGameService;
        this.scoreService = scoreService;
        this.commentService = commentService;
    }

    @GetMapping("/guestbook/boardgames")
    public String getBoardGames(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "dateDesc") String sortOrder,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) String[] tags,
            HttpSession session, Model model) {

        DecimalFormat df = new DecimalFormat("0.0");

        // Pageable 설정 (정렬 조건 포함)
        Pageable pageable = PageRequest.of(page, size, getSortOrder(sortOrder));

        // 페이징 처리된 BoardGame 데이터 조회
        Page<BoardGame> boardGamesPage = boardGameService.searchByKeywordAndTags(keyword, tags, sortOrder, pageable);

        // DTO로 변환
        List<BoardGame> boardGames = boardGamesPage.stream()
                .map(game -> {
                    game.setFormattedScore(game.getScoreSum() != null && game.getScoreCount() != null ?
                            df.format(game.getScoreSum() / game.getScoreCount()) : "0.0");
                    game.setFormattedDate(game.getDate() != null ?
                            game.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A");

                    int score = game.getScoreSum() != null && game.getScoreCount() != null ?
                            Math.min(5, (int) Math.round(game.getScoreSum() / game.getScoreCount())) : 0;
                    game.setStarRating(score);

                    // scoreRatio 계산
                    game.setScoreRatio(game.getScoreSum() != null && game.getScoreCount() != null ?
                            game.getScoreSum() / (double) game.getScoreCount() : 0.0);

                    return game;
                })
                .collect(Collectors.toList());

        // 선택된 태그를 문자열로 변환
        String tagsStr = tags != null ? String.join(",", tags) : "";

        model.addAttribute("boardGames", boardGames);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", boardGamesPage.getTotalPages());
        model.addAttribute("totalItems", boardGamesPage.getTotalElements());
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("keyword", keyword);
        model.addAttribute("tags", tagsStr);
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);

        return "guestbook/boardgames";
    }

    @GetMapping("/guestbook/boardgames/{id}")
    public String getBoardGameDetails(@PathVariable Long id, Model model, HttpSession session) {
        BoardGame boardGame = boardGameService.getBoardGameById(id);
        if (boardGame == null) {
            return "redirect:/guestbook/boardgames";
        }

        DecimalFormat df = new DecimalFormat("0.0");
        boardGame.setFormattedScore(boardGame.getScoreSum() != null && boardGame.getScoreCount() != null ?
                df.format(boardGame.getScoreSum() / boardGame.getScoreCount()) : "0.0");
        boardGame.setFormattedDate(boardGame.getDate() != null ?
                boardGame.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "N/A");

        int score = boardGame.getScoreSum() != null && boardGame.getScoreCount() != null ?
                Math.min(5, (int) Math.round(boardGame.getScoreSum() / boardGame.getScoreCount())) : 0;
        boardGame.setStarRating(score);
        boardGame.setScoreRatio(boardGame.getScoreSum() != null && boardGame.getScoreCount() != null ?
                boardGame.getScoreSum() / (double) boardGame.getScoreCount() : 0.0);

        // 댓글 목록 조회
        List<Comment> comments = commentService.getCommentsByBoardGameId(id);

        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            Score userScoreEntity = scoreService.findScoreByMemberAndBoardGame(Long.valueOf(loggedInUser.getId()), id);
            Integer userScore = userScoreEntity != null ? userScoreEntity.getScore() : null;
            model.addAttribute("userScore", userScore);
        }
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("boardGame", boardGame);
        model.addAttribute("comments", comments); // 댓글 목록을 모델에 추가

        return "guestbook/boardgame-details";
    }

    @PostMapping("/guestbook/boardgames/{id}/comments")
    public String addComment(
            @PathVariable("id") Long boardGameId,
            @RequestParam("content") String content,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Get the logged-in user
        String writerNum = (String) session.getAttribute("userNum");
        String writer = (String) session.getAttribute("userName");

        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        try {
            writerNum = String.valueOf(Long.valueOf(loggedInUser.getId()));
            writer = String.valueOf((loggedInUser.getNickname()));
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "잘못된 사용자 ID 형식입니다.");
            return "redirect:/guestbook/boardgames/" + boardGameId;
        }

        if (writerNum == null || writer == null) {
            redirectAttributes.addFlashAttribute("error", "로그인 후 댓글을 작성할 수 있습니다.");
            return "redirect:/Member/loginpage";
        }

        // Create a new comment entity
        Comment comment = new Comment();
        comment.setBgcBgid(boardGameId.toString());
        comment.setBgcWriternum(writerNum);
        comment.setBgcWriter(writer);
        comment.setBgcContent(content);

        // Save the comment
        try {
            commentService.addComment(comment);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "댓글 작성 중 오류가 발생했습니다.");
            return "redirect:/guestbook/boardgames/" + boardGameId;
        }

        redirectAttributes.addFlashAttribute("message", "댓글이 등록되었습니다.");
        return "redirect:/guestbook/boardgames/" + boardGameId;
    }





    @GetMapping("/guestbook/boardgames/create")
    public String showCreateBoardGameForm(Model model) {
        model.addAttribute("boardGame", new BoardGame());
        return "guestbook/create-boardgame";
    }

    @PostMapping("/guestbook/boardgames")
    public String createBoardGame(@ModelAttribute BoardGame boardGame, HttpSession session) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/Member/loginpage";
        }
        boardGame.setWriter(loggedInUser.getUsername());
        boardGame.setWriterNum(loggedInUser.getId());

        if (boardGame.getDate() != null) {
            try {
                LocalDateTime date = LocalDateTime.parse(boardGame.getDate().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                boardGame.setDate(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String tagsString = boardGame.getTag();
        if (tagsString != null && !tagsString.isEmpty()) {
            String[] tagsArray = tagsString.split(",");
            boardGame.setTag(String.join(",", tagsArray));
        }

        boardGameService.createBoardGame(boardGame);
        return "redirect:/guestbook/boardgames";
    }

    @GetMapping("/guestbook/boardgames/edit/{id}")
    public String showEditBoardGameForm(@PathVariable Long id, Model model) {
        BoardGame boardGame = boardGameService.getBoardGameById(id);
        if (boardGame == null) {
            return "redirect:/guestbook/boardgames";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String formattedDate = boardGame.getDate() != null ? boardGame.getDate().format(formatter) : "";

        model.addAttribute("boardGame", boardGame);
        model.addAttribute("formattedDate", formattedDate);
        return "guestbook/edit-boardgame";
    }

    @PostMapping("/guestbook/boardgames/edit")
    public String updateBoardGame(@ModelAttribute BoardGame updatedBoardGame, HttpSession session) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/Member/loginpage";
        }

        BoardGame existingBoardGame = boardGameService.getBoardGameById(updatedBoardGame.getId());
        if (existingBoardGame == null) {
            return "redirect:/guestbook/boardgames";
        }

        existingBoardGame.setTitle(updatedBoardGame.getTitle());
        existingBoardGame.setDate(updatedBoardGame.getDate());
        existingBoardGame.setContent(updatedBoardGame.getContent());
        if (updatedBoardGame.getTag() != null && !updatedBoardGame.getTag().isEmpty()) {
            String[] newTags = updatedBoardGame.getTag().split(",");
            String[] existingTags = existingBoardGame.getTag() != null ? existingBoardGame.getTag().split(",") : new String[]{};
            Set<String> tagSet = new HashSet<>(Arrays.asList(existingTags));
            tagSet.addAll(Arrays.asList(newTags));
            existingBoardGame.setTag(String.join(",", tagSet));
        }

        existingBoardGame.setWriter(loggedInUser.getUsername());
        existingBoardGame.setWriterNum(loggedInUser.getId());

        boardGameService.updateBoardGame(existingBoardGame);
        return "redirect:/guestbook/boardgames";
    }

    @PostMapping("/guestbook/boardgames/delete")
    public String deleteBoardGame(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            // 게시글에 달린 댓글 삭제
            commentService.deleteCommentsByBoardGameId(id);

            // 게시글 삭제
            boardGameService.deleteBoardGame(id);

            redirectAttributes.addFlashAttribute("message", "게시글과 관련된 댓글이 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "게시글 삭제 중 오류가 발생했습니다.");
        }
        return "redirect:/guestbook/boardgames";
    }


    private Sort getSortOrder(String sortOrder) {
        switch (sortOrder) {
            case "scoreDesc":
                return Sort.by(Sort.Order.desc("score"));
            case "scoreAsc":
                return Sort.by(Sort.Order.asc("score"));
            case "dateAsc":
                return Sort.by(Sort.Order.asc("date"));
            case "dateDesc":
            default:
                return Sort.by(Sort.Order.desc("date"));
        }
    }

    @PostMapping("/guestbook/boardgames/{id}/comments/edit/{commentId}")
    public String editComment(@PathVariable Long id,
                              @PathVariable Long commentId,
                              @RequestParam String content) {
        commentService.updateComment(commentId, content);
        return "redirect:/guestbook/boardgames/" + id;
    }

    @PostMapping("/guestbook/boardgames/{id}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long id, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/guestbook/boardgames/" + id;
    }

}
