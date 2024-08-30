package org.zerock.guestbook.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "board_free")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardGameTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "B_F_IDX")
    private Long id;

    @Column(name = "B_F_WRITER", nullable = false)
    private String writer;

    @Column(name = "B_F_WRITERNUM", nullable = false)
    private String writerNum;

    @Column(name = "B_F_TITLE", nullable = false)
    private String title;

}



