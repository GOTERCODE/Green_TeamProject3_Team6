package org.zerock.guestbook.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "B_F_LIKE")
@IdClass(BoardFreeLikeId.class)  // 복합 키를 사용하기 위해 IdClass 어노테이션 사용
@Data
@NoArgsConstructor  // 기본 생성자 추가
public class BoardFreeLike {

    @Id
    @Column(name = "BFL_MEMBERNUM", nullable = false)
    private Long memberNum;

    @Id
    @Column(name = "BFL_BOARDIDX", nullable = false)
    private Long boardIdx;

    @ManyToOne
    @JoinColumn(name = "BFL_MEMBERNUM", insertable = false, updatable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "BFL_BOARDIDX", insertable = false, updatable = false)
    private BoardFree boardFree;

    // 매개변수를 받는 생성자 추가
    public BoardFreeLike(Long memberNum, Long boardIdx) {
        this.memberNum = memberNum;
        this.boardIdx = boardIdx;
    }
}
