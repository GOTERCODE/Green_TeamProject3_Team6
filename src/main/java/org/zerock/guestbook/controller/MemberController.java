package org.zerock.guestbook.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.guestbook.entity.BoardGameTest;
import org.zerock.guestbook.entity.Member;
import org.zerock.guestbook.service.MemberService;
import org.zerock.guestbook.service.UserBoardGameService;

import java.util.List;

@RequestMapping("/Member")
@Controller
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private UserBoardGameService userboardgameservice;


    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        Member member = memberService.login(username, password);
        // 로그인 실패 시 로그인 페이지로 다시 이동합니다.
        if (member != null) {
            session.setAttribute("loggedInUser", member);
            return "redirect:/guestbook/newindex"; // 로그인 성공 시 홈 페이지로 리다이렉트합니다.
        }else{
            redirectAttributes.addFlashAttribute("loginError", "사용자 정보가 틀렸습니다");
            return "redirect:/Member/loginpage";
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
            redirectAttributes.addFlashAttribute("registration", "회원가입을 성공했습니다");
            return "redirect:/Member/loginpage"; // 성공 후 다른 페이지로 리다이렉트
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("registrationError", e.getMessage());
            return "redirect:/Member/loginpage";
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

    @GetMapping("/loginpage")
    public String loginpage(HttpSession session) {
        // 세션에서 로그인된 사용자 정보 가져오기
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");

        // 로그인된 사용자가 있으면 새로운 페이지로 리다이렉트
        if (loggedInUser != null) {
            return "redirect:/guestbook/newindex";
        }
        // 로그인 페이지로 이동
        return "/guestbook/Register_Test";
    }

     @GetMapping("/MyPage")
     public String MyPage(HttpSession session,Model model) {
            Member loggedInUser = (Member) session.getAttribute("loggedInUser");
            model.addAttribute("loggedInUser", loggedInUser);

            if (loggedInUser == null) {
                return "redirect:/Member/loginpage";
            }else{
                return "/guestbook/MyPage";
        }

    }

    @PostMapping("/userserch")
    public String userSerch(HttpSession session, Model model, HttpServletRequest request,
                            @RequestParam("userserchid") String userserchid,RedirectAttributes redirectAttributes) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("serchuserid", userserchid);

        if (loggedInUser == null) {
            return "redirect:/Member/loginpage";
        }

        List<BoardGameTest> boardGames = userboardgameservice.findByUsername(userserchid);
        if (boardGames.isEmpty()) {
            redirectAttributes.addFlashAttribute("NoUser", "해당 아이디의 유저가 등록한 글이 없습니다");
            return "redirect:" + request.getHeader("Referer");
        } else {
            model.addAttribute("boardGames", boardGames);
            // 현재 페이지로 유지
            return "/guestbook/UserSerch";
        }
    }

    @PostMapping("/updateProfile")
    public String updateProfile(@RequestParam("newuser_email") String email,
                                @RequestParam("newuser_nick") String nickname,
                                @RequestParam("newuser_id") String username,
                                @RequestParam("newuser_password") String password,
                                @RequestParam("newuser_password2") String password2,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {
        try {
            Member loggedInUser = (Member) session.getAttribute("loggedInUser");
            System.out.println(loggedInUser.getNickname());
            String oldnick = loggedInUser.getNickname();
            String currentPassword = loggedInUser.getPassword();
            System.out.println(currentPassword);
            if(password == null || password.isEmpty() || password2 == null || password2.isEmpty()) {
                Member updatedMember = memberService.updateMember2(email, nickname, username, currentPassword);
                // 세션에 현재 사용자 정보 갱신
                session.setAttribute("loggedInUser", updatedMember);
            }else{
                Member updatedMember = memberService.updateMember(email, nickname, username, password);
                session.setAttribute("loggedInUser", updatedMember);
            }
            memberService.updateBoard(oldnick, nickname);
            redirectAttributes.addFlashAttribute("updateSuccess", "회원정보가 성공적으로 수정되었습니다.");
            return "redirect:/Member/MyPage";
        } catch (IllegalArgumentException e) {
            // 오류 메시지와 함께 리다이렉트
            redirectAttributes.addFlashAttribute("updateError", e.getMessage());
            return "redirect:/Member/MyPage";
        }
    }

}
