package org.zerock.guestbook.service;

import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;

import java.util.List;

public interface GuestbookService {

//    Long register(GuestbookDTO dto);

    List<GuestbookDTO> getBestScoreGames();

    List<GuestbookDTO> getNewGames();

//    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO); // 추가된 메서드
}
