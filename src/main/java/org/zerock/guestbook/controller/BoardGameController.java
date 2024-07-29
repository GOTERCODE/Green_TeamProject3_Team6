package org.zerock.guestbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.zerock.guestbook.entity.BoardGame;
import org.zerock.guestbook.repository.BoardGameRepository;

import java.util.List;
import java.util.Base64;

@Controller
public class BoardGameController {

    private final BoardGameRepository boardGameRepository;

    public BoardGameController(BoardGameRepository boardGameRepository) {
        this.boardGameRepository = boardGameRepository;
    }

    @GetMapping("/guestbook/boardgames")
    public String listBoardGames(Model model) {
        List<BoardGame> boardGames = boardGameRepository.findAll();

        // Convert thumbnail to Base64
        for (BoardGame game : boardGames) {
            if (game.getThumbnail() != null) {
                String base64Thumbnail = Base64.getEncoder().encodeToString(game.getThumbnail());
                game.setThumbnailBase64(base64Thumbnail);
            }
        }

        model.addAttribute("boardGames", boardGames);
        return "guestbook/boardgames";
    }
}
