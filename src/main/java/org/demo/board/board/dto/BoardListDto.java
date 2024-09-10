package org.demo.board.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardListDto {

    private Long id;
    private String title;
    private String writer;
    private String content;
    private LocalDateTime regDate;
    private Long replyCount;
    private List<BoardImageDto> boardImageDtos;
}
