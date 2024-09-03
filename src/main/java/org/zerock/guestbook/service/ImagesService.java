package org.zerock.guestbook.service;

import org.springframework.stereotype.Service;
import org.zerock.guestbook.entity.Images;
import org.zerock.guestbook.repository.ImagesRepository;

import java.util.List;

@Service
public class ImagesService {

    private final ImagesRepository imageRepository;

    public ImagesService(ImagesRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public List<Images> getAllImages() {
        return imageRepository.findAll();
    }

    public void addImage(Images image) {
        imageRepository.save(image);
    }

    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }
}
