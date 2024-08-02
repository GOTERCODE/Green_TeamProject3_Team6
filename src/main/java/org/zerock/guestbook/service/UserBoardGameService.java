package org.zerock.guestbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.entity.BoardGame;
import org.zerock.guestbook.repository.UserBoardGameRepository;


@Service
public class UserBoardGameService {

    @Autowired
    private UserBoardGameRepository userboardgameRepository;


    public BoardGame findByUsername(String username) {
        return userboardgameRepository.findByWriter(username);
    }
}
