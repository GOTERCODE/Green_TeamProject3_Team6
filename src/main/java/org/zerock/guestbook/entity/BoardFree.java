package org.zerock.guestbook.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "board_free")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardFree {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "B_F_IDX")
    private Long id; // 변경: Long 타입으로 수정

    @Column(name = "B_F_WRITER", nullable = false)
    private String writer;

    @Column(name = "B_F_WRITERNUM", nullable = false)
    private String writer_num;

    @Column(name = "B_F_TITLE", nullable = false)
    private String title;

    @Column(name = "B_F_CONTENT", nullable = false)
    private String content;

    @Column(name = "B_F_DATE", nullable = false)
    private LocalDateTime date;

    @Column(name = "B_F_LIKE", nullable = true)
    private String like;


}
