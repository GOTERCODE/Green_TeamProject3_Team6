package org.zerock.guestbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.entity.BoardGame;
import org.zerock.guestbook.entity.News;
import org.zerock.guestbook.repository.NewsRepository;
import java.time.LocalDateTime;

import java.time.LocalDateTime;
import java.util.List;

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

    public News insert_news(String content,String title,String thumbnail , String comment, String tags, String writer, String writer_num){
        News news = News.builder()
                .title(title)
                .content(content)
                .thumbnail(thumbnail)
                .comment((comment)) // Convert String to LocalDateTime
                .category(tags)
                .writer(writer)
                .writer_num(writer_num)
                .date(LocalDateTime.now())
                .build();
        return newsRepository.save(news);
    }

    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }


    public News NewsPage_loading(Long id){
        return newsRepository.NewsPage_open(id);
    }

    public News News_re_edit(Long id) {
        return newsRepository.serach_news(id);
    }


    public News News_update(Long id, String title, String content, String thumbnail, String comment, String category) {
        newsRepository.update_news(id,title, content, thumbnail, comment, category);
        return newsRepository.NewsPage_open(id);
    }

    public List<News> getLatestNews() {
        return newsRepository.findTop4ByOrderByDateDesc();
    }

}

