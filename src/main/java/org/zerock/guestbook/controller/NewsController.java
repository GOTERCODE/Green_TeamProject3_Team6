package org.zerock.guestbook.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.guestbook.entity.Member;
import org.zerock.guestbook.entity.News;
import org.zerock.guestbook.service.MemberService;
import org.zerock.guestbook.service.NewsService;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequestMapping("/News")
@Controller
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/MainNew")
    public String getNews(
            HttpSession session,
            Model model,
            @RequestParam(defaultValue = "Allgame") String gameOrder,
            @RequestParam(defaultValue = "dateDesc") String sortOrder,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page
    ) {
        if (page < 0) {
            page = 0; // 페이지 번호가 음수일 때 기본값으로 설정
        }

        // Sort 객체 설정
        Sort sort;
        if ("dateAsc".equals(sortOrder)) {
            sort = Sort.by("date").ascending();
        } else {
            sort = Sort.by("date").descending();
        }

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, 10, sort);

        // 게임 카테고리 및 키워드에 따른 뉴스 검색
        Page<News> newsPage;
        if ("Allgame".equals(gameOrder)) {
            newsPage = newsService.searchNews(keyword, pageable);
        } else {
            newsPage = newsService.searchNewsByCategoryAndKeyword(gameOrder, keyword, pageable);
        }

        model.addAttribute("newsList", newsPage.getContent());
        model.addAttribute("totalPages", newsPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("gameOrder", gameOrder);
        model.addAttribute("sortOrder", sortOrder);

        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);

        return "guestbook/News"; // Thymeleaf 템플릿 경로
    }



    @GetMapping("/news_create")
    public String News_create(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Member loggedInUser;

        try {
            loggedInUser = (Member) session.getAttribute("loggedInUser");
            model.addAttribute("loggedInUser", loggedInUser);

            if (loggedInUser == null || !"admin".equals(loggedInUser.getUsername())) {
                redirectAttributes.addFlashAttribute("U_r_Not_admin", "뉴스 게시글은 관리자만 작성 가능합니다");
                return "redirect:/News/MainNew"; // 리다이렉트를 통해 News 페이지로 이동
            }
            return "guestbook/create-news";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("U_r_Not_admin", "뉴스 게시글은 관리자만 작성 가능합니다");
            return "redirect:/News/MainNew"; // 리다이렉트를 통해 News 페이지로 이동
        }
    }

    @PostMapping("/insert_news")
    public String insert_news(@RequestParam("content") String content,@RequestParam("title") String title,
                              @RequestParam("thumbnail") String thumbnail,@RequestParam("comment") String comment,
                              @RequestParam("tags") String tags,RedirectAttributes redirectAttributes,
                              HttpSession session, Model model){

        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        String writer = loggedInUser.getUsername();
        String writer_num = loggedInUser.getId();
        try{
            News insertnews = newsService.insert_news(content,title,thumbnail,comment,tags,writer,writer_num);
            redirectAttributes.addFlashAttribute("registration", "글쓰기를 성공했습니다");
            return "redirect:/News/MainNew";
        }catch(Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("registrationError", "글쓰기를 실패하였습니다");
            return "redirect:/News/news_create";
        }
    }


    @GetMapping("/NewsPage/{id}")
    public String NewsPage(HttpSession session, Model model,@PathVariable Long id){
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);

       try {
           News NewsPage_load = newsService.NewsPage_loading(id);
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
           String formattedDate = NewsPage_load.getDate().format(formatter);
           model.addAttribute("Date",formattedDate);
           model.addAttribute("news", NewsPage_load);
           return "guestbook/NewsPage";
       }catch (Exception e){
           e.printStackTrace();
           System.out.println("시발먼데");
           return "guestbook/newindex";
       }

    }

}
