package org.zerock.guestbook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "b_g_score")
@IdClass(ScoreId.class) // 복합 키 클래스를 지정
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Score {

    @Id
    @Column(name = "bgs_membernum")
    private Long memberNum;

    @Id
    @Column(name = "bgs_boardidx")
    private Long boardIdx;

    @Column(name = "bgs_score")
    private int score;
}
