package org.zerock.guestbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
                        RedirectAttributes redirectAttributes) {
        Member member = memberService.login(username, password);
        if (member != null) {
            session.setAttribute("loggedInUser", member);
            return "redirect:/guestbook/newindex"; // 로그인 성공 시 홈 페이지로 리다이렉트합니다.
        } else {
            redirectAttributes.addAttribute("loginError", "Invalid username or password");
            return "redirect:/Member/login"; // 로그인 실패 시 로그인 페이지로 다시 이동합니다.
        }
    }

    @PostMapping("/Register_Test")
    public String Register_Test(@RequestParam("userEmail") String userEmail,
                                @RequestParam("userNick") String userNick,
                                @RequestParam("register_username") String registerUsername,
                                @RequestParam("userpassword") String userPassword,
                                RedirectAttributes redirectAttributes) {
        try {
            Member newMember = memberService.Register_Test(userEmail, userNick, registerUsername, userPassword);
            redirectAttributes.addFlashAttribute("registration", "회원가입을 성공했습니다.");
            return "redirect:/Member/Register_Test"; // 성공 후 다른 페이지로 리다이렉트
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("registrationError", e.getMessage());
            return "redirect:/Member/Register_Test";
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
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
