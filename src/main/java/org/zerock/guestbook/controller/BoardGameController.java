package org.zerock.guestbook.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.guestbook.entity.BoardGame;
import org.zerock.guestbook.service.BoardGameService;
import org.zerock.guestbook.entity.Member;
import org.zerock.guestbook.service.MemberService;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BoardGameController {

    private final BoardGameService boardGameService;

    public BoardGameController(BoardGameService boardGameService) {
        this.boardGameService = boardGameService;
    }

    @GetMapping("/guestbook/boardgames")
    public String getBoardGames(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "dateDesc") String sortOrder,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) String[] tags,  // 태그 배열을 String[]로 수정
            HttpSession session,Model model) {

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
        model.addAttribute("tags", tagsStr);  // 선택된 태그 문자열을 모델에 추가
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);

        return "guestbook/boardgames";
    }



    private Sort getSortOrder(String sortOrder) {
        switch (sortOrder) {
            case "scoreDesc":
                return Sort.by(Sort.Order.desc("score")); // Entity에서의 정렬만 고려
            case "scoreAsc":
                return Sort.by(Sort.Order.asc("score")); // Entity에서의 정렬만 고려
            case "dateAsc":
                return Sort.by(Sort.Order.asc("date"));
            case "dateDesc":
            default:
                return Sort.by(Sort.Order.desc("date"));
        }
    }

    @GetMapping("/guestbook/boardgames/{id}")
    public String getBoardGameDetails(@PathVariable Long id, Model model) {
        BoardGame boardGame = boardGameService.getBoardGameById(id);
        if (boardGame == null) {
            return "redirect:/guestbook/boardgames";
        }

        DecimalFormat df = new DecimalFormat("0.0");
        boardGame.setFormattedScore(boardGame.getScoreSum() != null && boardGame.getScoreCount() != null ?
                df.format(boardGame.getScoreSum() / boardGame.getScoreCount()) : "0.0");
        boardGame.setFormattedDate(boardGame.getDate() != null ?
                boardGame.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "N/A");
// HH:mm:ss
        int score = boardGame.getScoreSum() != null && boardGame.getScoreCount() != null ?
                Math.min(5, (int) Math.round(boardGame.getScoreSum() / boardGame.getScoreCount())) : 0;
        boardGame.setStarRating(score);
        boardGame.setScoreRatio(boardGame.getScoreSum() != null && boardGame.getScoreCount() != null ?
                boardGame.getScoreSum() / (double) boardGame.getScoreCount() : 0.0);

        model.addAttribute("boardGame", boardGame);
        return "guestbook/boardgame-details"; // Ensure this matches the template filename
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
        boardGame.setWriter(loggedInUser.getUsername()); // 세션의 사용자 이름을 작성자로 설정
        boardGame.setWriterNum(loggedInUser.getId()); // 세션의 사용자 ID를 작성자 번호로 설정

        // Date 파싱 및 변환
        if (boardGame.getDate() != null) {
            try {
                LocalDateTime date = LocalDateTime.parse(boardGame.getDate().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                boardGame.setDate(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 태그를 처리합니다. 콤마로 구분된 문자열로 들어오는 경우
        String tagsString = boardGame.getTag(); // getTag()가 tags를 반환하는지 확인합니다
        if (tagsString != null && !tagsString.isEmpty()) {
            String[] tagsArray = tagsString.split(",");
            boardGame.setTag(String.join(",", tagsArray)); // 태그를 콤마로 구분된 문자열로 설정
        }

        boardGameService.createBoardGame(boardGame);
        return "redirect:/guestbook/boardgames";
    }

}








