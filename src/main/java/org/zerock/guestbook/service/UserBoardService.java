package org.zerock.guestbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.entity.Board_game;
import org.zerock.guestbook.repository.UserBoardRepository;

@Service
public class UserBoardService {


    @Autowired
    private UserBoardRepository userboardrepository;

    public Board_game board_game(String username) {
        return userboardrepository.findByUsername(username);
    }
}
