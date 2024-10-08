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
import org.zerock.guestbook.entity.BoardFree;
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

    public BoardFreeController(BoardFreeService boardFreeService, Comment_FService comment_fService, BoardFreeLikeService boardFreeLikeService) {
        this.boardFreeService = boardFreeService;
        this.comment_fService = comment_fService;
        this.boardFreeLikeService = boardFreeLikeService;
    }

    @GetMapping
    public String getAllBoardFree(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "searchType", required = false, defaultValue = "title") String searchType,
            Model model,
            HttpSession session) {

        Member loggedInUser = (Member) session.getAttribute("loggedInUser");

        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").descending());

        Page<BoardFree> boardFreePage;

        if (keyword != null && !keyword.isEmpty()) {
            switch (searchType) {
                case "content":
                    boardFreePage = boardFreeService.searchByContent(keyword, pageable);
                    break;
                case "writer":
                    boardFreePage = boardFreeService.searchByWriter(keyword, pageable);
                    break;
                case "title":
                default:
                    boardFreePage = boardFreeService.searchByTitle(keyword, pageable);
                    break;
            }
        } else {
            boardFreePage = boardFreeService.getAllBoardFree(pageable);
        }

        model.addAttribute("boardFreePage", boardFreePage);
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType);




        return "guestbook/boardfree_list";
    }

    @GetMapping("/{id}")
    public String getBoardFreeDetails(@PathVariable Long id, Model model, HttpSession session) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        BoardFree boardFree = boardFreeService.getBoardFreeById(id);
        Long likeCount = boardFreeLikeService.countLikes(id);

        boolean hasLiked = false;
        if (loggedInUser != null) {
            Long userId = Long.valueOf(loggedInUser.getId());
            hasLiked = boardFreeLikeService.hasLiked(userId, id);
        }

        List<Comment_F> comments = comment_fService.getCommentsByBoardFreeId(id);

        model.addAttribute("boardFree", boardFree);
        model.addAttribute("comments", comments);
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("hasLiked", hasLiked);

        return "guestbook/boardfree_view";
    }

    @GetMapping("/create")
    public String createBoardFreeForm(Model model, HttpSession session) {
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
        comment_fService.deleteCommentsBybfcBfid(id.toString());
        boardFreeService.deleteBoardFree(id);
        return "redirect:/boardfree";
    }

    @PostMapping("/{id}/comments")
    public String addComment(
            @PathVariable("id") Long boardFreeId,
            @RequestParam("content") String content,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "로그인 후 댓글을 작성할 수 있습니다.");
            return "redirect:/Member/loginpage";
        }

        Comment_F comment_f = new Comment_F();
        comment_f.setBfcBfid(boardFreeId.toString());
        comment_f.setBfcWriternum(String.valueOf(loggedInUser.getId()));
        comment_f.setBfcWriter(loggedInUser.getNickname());
        comment_f.setBfcContent(content);

        try {
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
            @PathVariable("id") Long boardFreeId,
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
    public String likePost(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/Member/loginpage";
        }

        Long memberNum = Long.valueOf(loggedInUser.getId());

        boolean isLiked = boardFreeLikeService.toggleLike(memberNum, id);

        if (isLiked) {
            redirectAttributes.addFlashAttribute("message", "추천되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("message", "추천이 취소되었습니다.");
        }

        model.addAttribute("isLiked", isLiked);

        return "redirect:/boardfree/" + id;
    }
}
