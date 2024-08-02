package org.zerock.guestbook.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.guestbook.entity.Member;
import org.zerock.guestbook.entity.Score;
import org.zerock.guestbook.service.ScoreService;

@Controller
public class ScoreController {

    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @PostMapping("/guestbook/boardgames/{id}/rate")
    public String rateBoardGame(
            @PathVariable Long id,
            @RequestParam int score,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Get the logged-in user
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "로그인 후 평가할 수 있습니다.");
            return "redirect:/guestbook/boardgames/" + id;
        }

        // Convert logged-in user ID from String to Long if necessary
        Long memberId = null;
        try {
            memberId = Long.valueOf(loggedInUser.getId());
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "잘못된 사용자 ID 형식입니다.");
            return "redirect:/guestbook/boardgames/" + id;
        }

        // Create or update the score entity
        Score scoreEntity = Score.builder()
                .memberNum(memberId) // Ensure memberNum is Long
                .boardIdx(id)  // Ensure boardIdx is Long
                .score(score)
                .build();

        scoreService.saveOrUpdateScore(scoreEntity);

        redirectAttributes.addFlashAttribute("message", "평가가 등록되었습니다.");
        return "redirect:/guestbook/boardgames/" + id;
    }

    @PostMapping("/guestbook/boardgames/{id}/deleteScore")
    public String deleteScore(
            @PathVariable Long id,
            @RequestParam Long memberNum,
            RedirectAttributes redirectAttributes) {

        scoreService.deleteScore(memberNum, id);

        redirectAttributes.addFlashAttribute("message", "점수가 삭제되었습니다.");
        return "redirect:/guestbook/boardgames/" + id;
    }
}
