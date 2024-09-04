package org.demo.board.board.service;

import org.demo.board.board.dto.ReplyDto;

public interface ReplyService {

    // 리플 등록하기
    Long register(ReplyDto replyDto);
}
