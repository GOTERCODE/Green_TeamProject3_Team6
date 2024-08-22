package org.zerock.guestbook.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.guestbook.entity.BoardFree;
import org.zerock.guestbook.entity.Comment;
import org.zerock.guestbook.entity.Comment_F;
import org.zerock.guestbook.entity.Member;
import org.zerock.guestbook.service.BoardFreeLikeService;
import org.zerock.guestbook.service.BoardFreeService;
import org.zerock.guestbook.service.Comment_FService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/boardfree")
public class BoardFreeController {


    private final BoardFreeService boardFreeService;
    private final Comment_FService comment_fService;
    private final BoardFreeLikeService boardFreeLikeService;

    public BoardFreeController(BoardFreeService boardFreeService,Comment_FService comment_fService,BoardFreeLikeService boardFreeLikeService){
        this.boardFreeService = boardFreeService;
        this.comment_fService = comment_fService;
        this.boardFreeLikeService = boardFreeLikeService;
    }

    @GetMapping
    public String getAllBoardFree(Model model,HttpSession session) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        List<BoardFree> boardFreeList = boardFreeService.getAllBoardFree();

        model.addAttribute("boardFreeList", boardFreeList);
        model.addAttribute("loggedInUser", loggedInUser);
        return "guestbook/boardfree_list";
    }

    @GetMapping("/{id}")
    public String getBoardFreeDetails(@PathVariable Long id, Model model, HttpSession session) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        BoardFree boardFree = boardFreeService.getBoardFreeById(id);
        Long likeCount = boardFreeLikeService.countLikes(id);

        // Comment_FService를 통해 댓글 리스트를 가져옵니다.
        List<Comment_F> comments = comment_fService.getCommentsByBoardFreeId(id);

        model.addAttribute("boardFree", boardFree);
        model.addAttribute("comments", comments); // 댓글 리스트를 모델에 추가합니다.
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("likeCount", likeCount);

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
        boardFree.setWriter(loggedInUser.getNickname());
        boardFree.setWriter_num(loggedInUser.getId());

        boardFree.setDate(LocalDateTime.now());
        boardFreeService.createBoardFree(boardFree);
        return "redirect:/boardfree";
    }

    @GetMapping("/edit/{id}")
    public String editBoardFreeForm(@PathVariable Long id, Model model, HttpSession session) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        BoardFree boardFree = boardFreeService.getBoardFreeById(id);
        model.addAttribute("boardFree", boardFree);
        model.addAttribute("loggedInUser", loggedInUser);
        return "guestbook/boardfree_update";
    }

    @PostMapping("/edit/{id}")
    public String editBoardFree(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "로그인 후 게시글을 수정할 수 있습니다.");
            return "redirect:/Member/loginpage";
        }

        try {
            BoardFree boardFree = boardFreeService.getBoardFreeById(id);
            boardFree.setTitle(title);
            boardFree.setContent(content);
            boardFreeService.updateBoardFree(boardFree);
            redirectAttributes.addFlashAttribute("message", "게시글이 수정되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "게시글 수정 중 오류가 발생했습니다.");
        }

        return "redirect:/boardfree/" + id;
    }

    @PostMapping("/delete")
    public String deleteBoardFree(@RequestParam Long id) {
        boardFreeService.deleteBoardFree(id);
        return "redirect:/boardfree";
    }

    @PostMapping("/{id}/comments")
    public String addComment(

            @PathVariable("id") Long boardFreeId,
            @RequestParam("content") String content,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Get the logged-in user
        String writerNum = (String) session.getAttribute("userNum");
        String writer = (String) session.getAttribute("userName");

        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        try {

            writerNum = String.valueOf(Long.valueOf(loggedInUser.getId()));
            writer = String.valueOf((loggedInUser.getNickname()));
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "잘못된 사용자 ID 형식입니다.");
            return "redirect:/" + boardFreeId;
        }

        if (writerNum == null || writer == null) {
            redirectAttributes.addFlashAttribute("error", "로그인 후 댓글을 작성할 수 있습니다.");
            return "redirect:/Member/loginpage";
        }

        // Create a new comment entity
        Comment_F comment_f = new Comment_F();
        comment_f.setBfcBfid(boardFreeId.toString());
        comment_f.setBfcWriternum(writerNum);
        comment_f.setBfcWriter(writer);
        comment_f.setBfcContent(content);

        // Save the comment
        try {
            System.out.println("debug - " + content);
            System.out.println("debug - " + writerNum);
            comment_fService.addComment(comment_f);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "댓글 작성 중 오류가 발생했습니다.");
            return "redirect:/boardfree/" + boardFreeId;
        }

        redirectAttributes.addFlashAttribute("message", "댓글이 등록되었습니다.");
        return "redirect:/boardfree/" + boardFreeId;
    }
    @PostMapping("/{id}/deletecomments/{commentId}")
    public String deleteComment(
            @PathVariable("id") Long boardFreeId,  // boardFreeId 수정
            @PathVariable("commentId") Long commentId,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        Member loggedInUser = (Member) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "로그인 후 댓글을 삭제할 수 있습니다.");
            return "redirect:/Member/loginpage";
        }

        try {
            comment_fService.deleteComment(commentId);
            redirectAttributes.addFlashAttribute("message", "댓글이 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "댓글 삭제 중 오류가 발생했습니다.");
        }

        return "redirect:/boardfree/" + boardFreeId;
    }

    @PostMapping("/{id}/updatecomments/{commentId}")
    public String updateComment(
            @PathVariable("id") Long boardFreeId,
            @PathVariable("commentId") Long commentId,
            @RequestParam("content") String content,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        Member loggedInUser = (Member) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "로그인 후 댓글을 수정할 수 있습니다.");
            return "redirect:/Member/loginpage";
        }

        try {
            comment_fService.updateComment(commentId, content);
            redirectAttributes.addFlashAttribute("message", "댓글이 수정되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "댓글 수정 중 오류가 발생했습니다.");
        }

        return "redirect:/boardfree/" + boardFreeId;
    }


    @PostMapping("/{id}/like")
    public String likePost(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login"; // 로그인이 필요할 경우 리다이렉트
        }

        Long memberNum = Long.valueOf(loggedInUser.getId());

        boolean isLiked = boardFreeLikeService.toggleLike(memberNum, id);

        if (isLiked) {
            redirectAttributes.addFlashAttribute("message", "추천되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("message", "추천이 취소되었습니다.");
        }

        return "redirect:/boardfree/" + id; // 리다이렉트 URL이 정확한지 확인
    }









}
