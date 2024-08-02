package org.zerock.guestbook.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "board_game")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "B_G_IDX")
    private Long id;

    @Column(name = "B_G_WRITER", nullable = false)
    private String writer;

    @Column(name = "B_G_WRITERNUM", nullable = false)
    private String writerNum;

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

    @Transient
    public List<String> getTagList() {
        if (tag != null && !tag.isEmpty()) {
            return Arrays.asList(tag.split(","));
        }
        return Collections.emptyList();
    }

    @Column(name = "B_G_GAMETITLE")
    private String gameTitle;

    @Column(name = "B_G_SCORE", precision = 5, scale = 2)
    private Double score;

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


    @Transient
    private Double scoreRatio;

    public void addScore(Double score) {
        if (scoreSum == null) scoreSum = 0.0;
        if (scoreCount == null) scoreCount = 0;

        scoreSum += score;
        scoreCount++;
    }

    public void removeScore(Double score) {
        if (scoreSum == null || scoreCount == null) return;

        scoreSum -= score;
        scoreCount--;
    }

}



