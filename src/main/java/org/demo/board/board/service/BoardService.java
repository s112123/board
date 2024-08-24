package org.demo.board.board.service;

import org.demo.board.board.dto.BoardDto;

public interface BoardService {

    // 등록하기
    Long addBoard(BoardDto boardDto);
}
