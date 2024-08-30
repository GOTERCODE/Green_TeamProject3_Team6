package org.zerock.guestbook.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "b_f_like")
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class B_F_like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BFL_MEMBERNUM")
    private Long id; // 변경: Long 타입으로 수정

    @Column(name = "BFL_BOARDIDX", nullable = false)
    private String writer;

}
