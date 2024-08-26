package org.demo.board.board.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class BoardDto {

    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    @NotEmpty
    private String writer;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    // 첨부파일의 이름들: Board에서 Set<BoardImage> 타입으로 변환되어야 한다
    // <input>의 name과 동일하게 변수명 작성
    private List<String> fileNames;
}
