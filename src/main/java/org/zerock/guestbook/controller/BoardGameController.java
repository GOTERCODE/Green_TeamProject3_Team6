package org.zerock.guestbook.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.guestbook.entity.BoardGame;
import org.zerock.guestbook.service.BoardGameService;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BoardGameController {

    private final BoardGameService boardGameService;

    @GetMapping("/guestbook/boardgames")
    public String getBoardGames(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {

        DecimalFormat df = new DecimalFormat("0.0");
        Pageable pageable = PageRequest.of(page, size);

        // 페이징 처리된 BoardGame 데이터 조회
        Page<BoardGame> boardGamesPage = boardGameService.findAll(pageable);

        // DTO로 변환
        var boardGames = boardGamesPage.stream()
                .map(game -> {
                    game.setFormattedScore(game.getScoreSum() != null && game.getScoreCount() != null ?
                            df.format(game.getScoreSum() / game.getScoreCount()) : "0.0");
                    game.setFormattedDate(game.getDate() != null ?
                            game.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A");

                    int score = game.getScoreSum() != null && game.getScoreCount() != null ?
                            Math.min(5, (int) Math.round(game.getScoreSum() / game.getScoreCount())) : 0;
                    game.setStarRating(score);
                    return game;
                })
                .collect(Collectors.toList());

        model.addAttribute("boardGames", boardGames);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", boardGamesPage.getTotalPages());
        model.addAttribute("totalItems", boardGamesPage.getTotalElements());

        return "guestbook/boardgames";
    }
}
