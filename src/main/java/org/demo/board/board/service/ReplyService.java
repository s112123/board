package org.demo.board.board.service;

import org.demo.board.board.dto.PageRequestDto;
import org.demo.board.board.dto.PageResponseDto;
import org.demo.board.board.dto.ReplyDto;

public interface ReplyService {

    // 리플 등록하기
    Long register(ReplyDto replyDto);

    // 리플 목록조회
    PageResponseDto<ReplyDto> getReplies(Long boardId, PageRequestDto pageRequestDto);

    // 리플 조회하기
    ReplyDto read(Long id);

    // 리플 수정하기
    void modify(ReplyDto replyDto);

    // 리플 삭제하기
    void remove(Long id);
}
