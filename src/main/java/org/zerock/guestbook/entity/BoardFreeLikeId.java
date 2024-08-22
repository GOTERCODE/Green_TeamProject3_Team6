package org.zerock.guestbook.entity;

import java.io.Serializable;
import java.util.Objects;

public class BoardFreeLikeId implements Serializable {

    private Long memberNum;
    private Long boardIdx;

    public BoardFreeLikeId() {
    }

    public BoardFreeLikeId(Long memberNum, Long boardIdx) {
        this.memberNum = memberNum;
        this.boardIdx = boardIdx;
    }

    // equals()와 hashCode()를 적절히 구현하여 복합 키 비교를 지원합니다.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardFreeLikeId that = (BoardFreeLikeId) o;
        return Objects.equals(memberNum, that.memberNum) &&
                Objects.equals(boardIdx, that.boardIdx);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberNum, boardIdx);
    }
}
