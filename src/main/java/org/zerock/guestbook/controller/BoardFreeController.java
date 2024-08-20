package org.zerock.guestbook.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.guestbook.entity.BoardFree;
import org.zerock.guestbook.entity.Member;
import org.zerock.guestbook.service.BoardFreeService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/boardfree")
public class BoardFreeController {

    @Autowired
    private BoardFreeService boardFreeService;

    @GetMapping
    public String getAllBoardFree(Model model,HttpSession session) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        List<BoardFree> boardFreeList = boardFreeService.getAllBoardFree();
        model.addAttribute("boardFreeList", boardFreeList);
        model.addAttribute("loggedInUser", loggedInUser);
        return "guestbook/boardfree_list";
    }

    @GetMapping("/{id}")
    public String getBoardFreeDetails(@PathVariable Long id, Model model,HttpSession session) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        BoardFree boardFree = boardFreeService.getBoardFreeById(id);
        model.addAttribute("boardFree", boardFree);
        model.addAttribute("loggedInUser", loggedInUser);
        return "guestbook/boardfree_view";
    }

    @GetMapping("/create")
    public String createBoardFreeForm(Model model,HttpSession session) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        model.addAttribute("boardFree", new BoardFree());
        model.addAttribute("loggedInUser", loggedInUser);
        return "/guestbook/boardfree_create";
    }

    @PostMapping("/create")
    public String createBoardFree(@ModelAttribute BoardFree boardFree, HttpSession session) {

        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/Member/loginpage";
        }
        boardFree.setWriter(loggedInUser.getUsername());
        boardFree.setWriter_num(loggedInUser.getId());

        boardFree.setDate(LocalDateTime.now());
        boardFreeService.createBoardFree(boardFree);
        return "redirect:/boardfree";
    }

    @GetMapping("/edit/{id}")
    public String editBoardFreeForm(@PathVariable Long id, Model model,HttpSession session) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        BoardFree boardFree = boardFreeService.getBoardFreeById(id);
        model.addAttribute("boardFree", boardFree);
        model.addAttribute("loggedInUser", loggedInUser);
        return "boardfree/edit";
    }

    @PostMapping("/edit")
    public String editBoardFree(@ModelAttribute BoardFree boardFree,HttpSession session) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        boardFreeService.updateBoardFree(boardFree);

        return "redirect:/boardfree";
    }

    @PostMapping("/delete")
    public String deleteBoardFree(@RequestParam Long id) {
        boardFreeService.deleteBoardFree(id);
        return "redirect:/boardfree";
    }

}
