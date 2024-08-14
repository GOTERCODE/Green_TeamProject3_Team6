package org.zerock.guestbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.entity.News;
import org.zerock.guestbook.repository.NewsRepository;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    public Page<News> searchNews(String keyword, Pageable pageable) {
        return newsRepository.findByTitleContaining(keyword, pageable);
    }

    public Page<News> searchNewsByCategoryAndKeyword(String category, String keyword, Pageable pageable) {
        return newsRepository.findByCategoryAndTitleContaining(category, keyword, pageable);
    }
}

