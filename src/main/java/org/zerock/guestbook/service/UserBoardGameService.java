package org.zerock.guestbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.entity.BoardGameTest;
import org.zerock.guestbook.repository.UserBoardGameRepository;

import java.util.List;


@Service
public class UserBoardGameService {

    @Autowired
    private UserBoardGameRepository userboardgameRepository;


    public List<BoardGameTest> findByUsername(String username) {
        return userboardgameRepository.findByWriterIgnoreCase(username);
    }

}
