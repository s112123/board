package org.demo.board.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardImageDto {

    private String uuid;
    private int fileIndex;
    private String fileName;
}
