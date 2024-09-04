package org.demo.board.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyDto {

    private Long id;
    @NotNull
    private Long boardId;
    @NotEmpty
    private String text;
    @NotEmpty
    private String writer;
    // JSON 처리시에 Formatting 지정
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate;
    // 댓글 수정 시간은 화면에 출력할 필요없으므로 JSON에서 제외
    @JsonIgnore
    private LocalDateTime modDate;
}
