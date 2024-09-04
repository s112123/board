package org.demo.board.board.service;

import org.demo.board.board.dto.PageRequestDto;
import org.demo.board.board.dto.PageResponseDto;
import org.demo.board.board.dto.ReplyDto;

public interface ReplyService {

    // 리플 등록하기
    Long register(ReplyDto replyDto);

    // 리플 목록조회
    PageResponseDto<ReplyDto> getReplies(Long boardId, PageRequestDto pageRequestDto);
}
