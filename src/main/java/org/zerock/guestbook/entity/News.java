package org.zerock.guestbook.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "board_news")
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "B_N_IDX")
    private Long id; // 변경: Long 타입으로 수정

    @Column(name = "B_N_WRITER", nullable = false)
    private String writer;

    @Column(name = "B_N_WRITERNUM", nullable = false)
    private String writer_num;

    @Column(name = "B_N_TITLE", nullable = false)
    private String title;

    @Column(name = "B_N_CONTENT", nullable = false)
    private String content;

    @Column(name = "B_N_THUMBNAIL", nullable = false)
    private String thumbnail;

    @Column(name = "B_N_DATE", nullable = false)
    private LocalDateTime date;

    @Column(name = "B_N_LIKE", nullable = false)
    private String like;

    @Column(name = "B_N_category", nullable = false)
    private String category;
}
