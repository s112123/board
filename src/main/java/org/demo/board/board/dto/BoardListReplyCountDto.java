package org.demo.board.board.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardListReplyCountDto {

    private Long id;
    private String title;
    private String writer;
    private String content;
    private LocalDateTime regDate;
    private Long replyCount;
}
