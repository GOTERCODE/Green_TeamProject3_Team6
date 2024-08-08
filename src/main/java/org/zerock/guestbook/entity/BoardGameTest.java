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
public class BoardGameTest {

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

}



