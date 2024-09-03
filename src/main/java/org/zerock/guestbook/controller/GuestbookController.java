package org.zerock.guestbook.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.entity.Images;
import org.zerock.guestbook.service.GuestbookService;
import org.zerock.guestbook.entity.Member;
import org.zerock.guestbook.service.NewsService;
import org.zerock.guestbook.service.BoardFreeService;
import org.zerock.guestbook.service.ImagesService;

import java.awt.*;
import java.util.List;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestbookController {

    private final GuestbookService service;
    private final NewsService newsService;
    private final BoardFreeService boardFreeService;
    private final ImagesService imagesService;

    @GetMapping("/")
    public String index() {
        return "redirect:/guestbook/newindex";
    }

    @GetMapping("/newindex")
    public String newIndex(PageRequestDTO pageRequestDTO, Model model, HttpSession session) {
        log.info("newindex............." + pageRequestDTO);

        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("loggedInUser", loggedInUser);
        } else {
            model.addAttribute("loginError", "You need to log in.");
        }

        model.addAttribute("bestScoreGames", service.getBestScoreGames());
        model.addAttribute("newGames", service.getNewGames());
        model.addAttribute("latestNews", newsService.getLatestNews());
        model.addAttribute("top5Posts", boardFreeService.getTop5BoardFree());

        // 이미지 정보를 가져와서 모델에 추가
        List<Images> images = imagesService.getAllImages();
        model.addAttribute("images", images);

        return "guestbook/newindex"; // Thymeleaf 템플릿 이름
    }

    @GetMapping("/basic")
    public String yourPage(HttpSession session, Model model) {
        // 세션에서 로그인된 사용자 정보 가져오기
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");

        // 모델에 로그인된 사용자 추가
        model.addAttribute("loggedInUser", loggedInUser);

        return "your-template"; // Thymeleaf 템플릿 이름
    }







}