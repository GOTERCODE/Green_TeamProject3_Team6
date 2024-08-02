package org.zerock.guestbook.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class Board_game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "B_G_IDX")
    private String board_id;

    @Column(name = "B_G_TITLE", unique = true, nullable = false)
    private String board_title;

    @Column(name = "B_G_WRITER") // 추가된 필드
    private String username;

}

