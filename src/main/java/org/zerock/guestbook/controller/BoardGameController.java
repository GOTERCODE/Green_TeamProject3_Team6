package org.zerock.guestbook.controller;

import org.zerock.guestbook.entity.BoardGame;
import org.zerock.guestbook.service.BoardGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String getBoardGames(Model model) {
        DecimalFormat df = new DecimalFormat("0.0");

        List<BoardGame> boardGames = boardGameService.findAll().stream()
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

        // 로그 추가
        System.out.println("Board Games: " + boardGames);

        model.addAttribute("boardGames", boardGames);
        return "guestbook/boardgames";
    }

}