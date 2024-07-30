package org.zerock.guestbook.controller;

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

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
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
            Model model) {

        DecimalFormat df = new DecimalFormat("0.0");

        // Pageable 설정
        Pageable pageable = PageRequest.of(page, size);

        // 페이징 처리된 BoardGame 데이터 조회
        Page<BoardGame> boardGamesPage = boardGameService.searchByKeyword(keyword, sortOrder, pageable);

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

        model.addAttribute("boardGames", boardGames);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", boardGamesPage.getTotalPages());
        model.addAttribute("totalItems", boardGamesPage.getTotalElements());
        model.addAttribute("sortOrder", sortOrder);  // 현재 정렬 조건 추가
        model.addAttribute("keyword", keyword);

        return "guestbook/boardgames";
    }
}