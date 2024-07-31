package org.zerock.guestbook.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.guestbook.entity.BoardGame;
import org.zerock.guestbook.service.BoardGameService;
import org.zerock.guestbook.entity.Member;
import org.zerock.guestbook.service.MemberService;

import java.text.DecimalFormat;
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
}
