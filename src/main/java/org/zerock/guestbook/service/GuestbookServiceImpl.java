package org.zerock.guestbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.repository.GuestbookRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuestbookServiceImpl implements GuestbookService {

    @Autowired
    private GuestbookRepository repository;

    @Override
    public List<GuestbookDTO> getBestScoreGames() {
        Pageable pageable = PageRequest.of(0, 3);  // 페이지 번호, 페이지 크기
        List<Guestbook> top3ByScore = repository.findTopByOrderByScoreDesc(pageable);

        return top3ByScore.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GuestbookDTO> getNewGames() {
        Pageable pageable = PageRequest.of(0, 3);  // 페이지 번호, 페이지 크기
        List<Guestbook> top3ByDate = repository.findTopByOrderByDateDesc(pageable);

        return top3ByDate.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    private GuestbookDTO entityToDto(Guestbook entity) {
        DecimalFormat df = new DecimalFormat("0.0");
        int score = entity.getScoreSum() != null && entity.getScoreCount() != null ?
                Math.min(5, (int) Math.round(entity.getScoreSum() / entity.getScoreCount())) : 0;
        String formattedScore = entity.getScoreSum() != null && entity.getScoreCount() != null ?
                df.format(entity.getScoreSum() / entity.getScoreCount()) : "0.0";

        return GuestbookDTO.builder()
                .gno(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .scoreSum(entity.getScoreSum())
                .scoreCount(entity.getScoreCount())
                .date(entity.getDate())
                .thumbnail(entity.getThumbnail())
                .formattedScore(formattedScore)
                .starRating(score)
                .build();
    }
}