package org.zerock.guestbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.guestbook.dto.PageRequestDTO;
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

    @GetMapping("/user_info_register")
    public String user_info_register(){
        return "guestbook/user_info_register";
    }

}
