package org.zerock.guestbook.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.service.GuestbookService;
import org.zerock.guestbook.entity.Member;


@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestbookController {

    private final GuestbookService service;


    @GetMapping("/login")
    public String login() {
        return "/guestbook/login"; // "login.html" 템플릿 파일을 반환
    }



    @GetMapping("/")
    public String index() {
        return "redirect:/guestbook/newindex";
    }

//    @GetMapping("/newindex")
//    public void newIndex(PageRequestDTO pageRequestDTO, Model model) {
//        log.info("newindex............." + pageRequestDTO);
//
//        model.addAttribute("result", service.getList(pageRequestDTO));
//    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {

        log.info("list............." + pageRequestDTO);

        model.addAttribute("result", service.getList(pageRequestDTO));
    }

    @GetMapping("/register")
    public void register() {
        log.info("regiser get...");
    }

    @PostMapping("/register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes) {
        log.info("dto..." + dto);

        Long gno = service.register(dto);
        redirectAttributes.addFlashAttribute("msg", gno);

        return "redirect:/guestbook/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
        log.info("gno: " + gno);

        GuestbookDTO dto = service.read(gno);

        model.addAttribute("dto", dto);
    }

    @PostMapping("/remove")
    public String remove(long gno, RedirectAttributes redirectAttributes) {
        log.info("gno: " + gno);

        service.remove(gno);
        redirectAttributes.addFlashAttribute("msg", gno);

        return "redirect:/guestbook/list";
    }

    @PostMapping("/modify")
    public String modify(GuestbookDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes) {
        log.info("post modify.....................");
        log.info("dto: " + dto);

        service.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("gno", dto.getGno());

        return "redirect:/guestbook/read";
    }


    @GetMapping("/newindex")
    public String newIndex(PageRequestDTO pageRequestDTO, Model model, HttpSession session) {
        log.info("newindex............." + pageRequestDTO);

        // 세션에서 로그인된 사용자 정보 가져오기
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");

        // 모델에 로그인된 사용자 추가
        if (loggedInUser != null) {
            model.addAttribute("loggedInUser", loggedInUser);
        }else {
            model.addAttribute("loginError", "You need to log in.");
        }

        // 페이지 요청 DTO를 모델에 추가
        model.addAttribute("result", service.getList(pageRequestDTO));

        return "guestbook/newindex"; // Thymeleaf 템플릿 이름
    }
}