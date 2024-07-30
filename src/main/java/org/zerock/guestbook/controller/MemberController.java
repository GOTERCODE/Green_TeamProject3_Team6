package org.zerock.guestbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.guestbook.entity.Member;
import org.zerock.guestbook.service.MemberService;


import jakarta.servlet.http.HttpSession;

@RequestMapping("/Member")
@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        Member member = memberService.login(username, password);
        if (member != null) {
            session.setAttribute("loggedInUser", member);
            return "redirect:/guestbook/newindex"; // 로그인 성공 시 홈 페이지로 리다이렉트합니다.
        } else {
            model.addAttribute("loginError", "Invalid username or password");
            return "redirect:/guestbook/login"; // 로그인 실패 시 로그인 페이지로 다시 이동합니다.
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/guestbook/newindex"; // 로그아웃 시 로그인 페이지로 리다이렉트합니다.
    }

    @GetMapping("/Register_Test")
    public String user_info_register(HttpSession session){
        // 세션에서 로그인된 사용자 정보 가져오기
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");

        // 로그인된 사용자가 있으면 새로운 페이지로 리다이렉트
        if (loggedInUser != null) {
            return "redirect:/guestbook/newindex";
        }

        return "/guestbook/Register_Test";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        // 세션에서 로그인된 사용자 정보 가져오기
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");

        // 로그인된 사용자가 있으면 새로운 페이지로 리다이렉트
        if (loggedInUser != null) {
            return "redirect:/guestbook/newindex";
        }

        // 로그인 페이지로 이동
        return "/guestbook/Register_Test";
    }

}
