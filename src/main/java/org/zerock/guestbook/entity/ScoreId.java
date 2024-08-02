package org.zerock.guestbook.entity;

import java.io.Serializable;
import java.util.Objects;

public class ScoreId implements Serializable {
    private Long memberNum;
    private Long boardIdx;

    // 기본 생성자
    public ScoreId() {}

    // 모든 필드를 사용하는 생성자
    public ScoreId(Long memberNum, Long boardIdx) {
        this.memberNum = memberNum;
        this.boardIdx = boardIdx;
    }

    // equals()와 hashCode() 메서드 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScoreId scoreId = (ScoreId) o;
        return Objects.equals(memberNum, scoreId.memberNum) &&
                Objects.equals(boardIdx, scoreId.boardIdx);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberNum, boardIdx);
    }

    // getters and setters
    public Long getMemberNum() { return memberNum; }
    public void setMemberNum(Long memberNum) { this.memberNum = memberNum; }
    public Long getBoardIdx() { return boardIdx; }
    public void setBoardIdx(Long boardIdx) { this.boardIdx = boardIdx; }
}
