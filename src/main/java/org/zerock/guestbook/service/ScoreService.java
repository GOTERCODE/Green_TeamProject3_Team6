package org.zerock.guestbook.service;

import org.zerock.guestbook.entity.Score;
import org.zerock.guestbook.entity.ScoreId;
import org.zerock.guestbook.repository.ScoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final BoardGameService boardGameService;

    public ScoreService(ScoreRepository scoreRepository, BoardGameService boardGameService) {
        this.scoreRepository = scoreRepository;
        this.boardGameService = boardGameService;
    }

    @Transactional
    public void saveOrUpdateScore(Score score) {
        Score existingScore = scoreRepository.findById(new ScoreId(score.getMemberNum(), score.getBoardIdx()))
                .orElse(null);

        if (existingScore != null) {
            // Existing score, remove it first
            boardGameService.updateBoardGameScore(score.getBoardIdx(), (double) existingScore.getScore(), false);
        }

        // Save new score
        scoreRepository.save(score);
        boardGameService.updateBoardGameScore(score.getBoardIdx(), (double) score.getScore(), true);
    }

    @Transactional
    public void deleteScore(Long memberNum, Long boardIdx) {
        Score existingScore = scoreRepository.findById(new ScoreId(memberNum, boardIdx))
                .orElse(null);

        if (existingScore != null) {
            scoreRepository.deleteById(new ScoreId(memberNum, boardIdx));
            boardGameService.updateBoardGameScore(boardIdx, (double) existingScore.getScore(), false);
        }
    }

    public Score findScoreByMemberAndBoardGame(Long memberId, Long boardGameId) {
        return scoreRepository.findById(new ScoreId(memberId, boardGameId)).orElse(null);
    }


}
