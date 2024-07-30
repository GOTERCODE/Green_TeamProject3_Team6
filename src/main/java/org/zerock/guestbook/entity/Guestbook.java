package org.zerock.guestbook.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "board_game")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guestbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "B_G_IDX")
    private Long id;

    @Column(name = "B_G_WRITER", nullable = false)
    private String writer;

    @Column(name = "B_G_WRITERNUM", nullable = false)
    private Integer writerNum;

    @Column(name = "B_G_TITLE", nullable = false)
    private String title;

    @Column(name = "B_G_CONTENT", columnDefinition = "TEXT")
    private String content;

    @Lob
    @Column(name = "B_G_THUMBNAIL")
    private String thumbnail;

    @Column(name = "B_G_SCORESUM", precision = 5, scale = 2)
    private Double scoreSum;

    @Column(name = "B_G_SCORECOUNT")
    private Integer scoreCount;

    @Column(name = "B_G_DATE")
    private LocalDateTime date;

    @Column(name = "B_G_TAG")
    private String tag;

    @Column(name = "B_G_GAMETITLE")
    private String gameTitle;


    @Transient
    private String formattedDate;  // 포맷된 날짜 문자열



    @Transient
    private String formattedScore;  // 소수점 포맷된 점수

    public String getFormattedScore() {
        return formattedScore;
    }

    public void setFormattedScore(String formattedScore) {
        this.formattedScore = formattedScore;
    }

    @Transient
    private int starRating;  // 별의 수

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;

    }
}