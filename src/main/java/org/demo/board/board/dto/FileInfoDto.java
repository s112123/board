package org.demo.board.board.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FileInfoDto {

    private String uuid;
    private String fileName;
    private boolean isImage;

    // JSON의 link 속성의 값에 첨부파일 경로 처리를 위해 서버에 저장된 파일 이름을 반환하는 메서드
    public String getLink() {
        // 이미지 파일인 경우, 썸네일을 이미지 파일 이름 반환
        if (isImage) {
            return "s_" + uuid + "_" + fileName;
        } else {
            return uuid + "_" + fileName;
        }
    }
}
