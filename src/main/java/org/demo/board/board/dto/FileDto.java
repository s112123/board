package org.demo.board.board.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class FileDto {

    // <input>의 name과 동일하게 변수명 작성한다
    private List<MultipartFile> files;
}
