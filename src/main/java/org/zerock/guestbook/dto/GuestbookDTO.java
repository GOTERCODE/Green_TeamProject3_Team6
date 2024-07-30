package org.zerock.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GuestbookDTO {
        private Long gno;  // 엔티티 ID
        private String title;
        private String content;
        private String writer;
        private Double scoreSum;  // 평점 합계
        private Integer scoreCount;  // 평점 개수
        private LocalDateTime date;  // 등록 날짜
        private String thumbnail;  // 썸네일 URL
        private String formattedScore;  // 포맷된 점수
        private int starRating;  // 별의 수
}
