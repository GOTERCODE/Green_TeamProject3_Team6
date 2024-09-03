package org.zerock.guestbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.guestbook.entity.Images;
import org.zerock.guestbook.service.ImagesService;

@Controller
@RequestMapping("/images")
public class ImagesController {

    private final ImagesService imagesService;

    public ImagesController(ImagesService imagesService) {
        this.imagesService = imagesService;
    }

    @GetMapping
    public String listImages(Model model) {
        model.addAttribute("images", imagesService.getAllImages());
        return "guestbook/newindex"; // 이미지가 표시될 템플릿 이름
    }

    @PostMapping("/add")
    public String addImage(@RequestParam String url) {
        Images image = new Images();
        image.setUrl(url);
        imagesService.addImage(image);
        return "redirect:/images";
    }

    @PostMapping("/delete")
    public String deleteImage(@RequestParam Long id) {
        imagesService.deleteImage(id);
        return "redirect:/images";
    }
}
