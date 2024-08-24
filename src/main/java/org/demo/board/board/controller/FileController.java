package org.demo.board.board.controller;

import lombok.extern.slf4j.Slf4j;
import org.demo.board.board.dto.FileDto;
import org.demo.board.board.dto.FileInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${file.upload.dir.path}")
    private String uploadDirPath;

    // 업로드 이미지 파일을 서버에 저장 → D:\\upload
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<FileInfoDto> uploadFile(FileDto fileDto) {
        if (fileDto.getFiles() != null) {
            // 업로드된 파일을 서버에 저장하고 FileInfoDto에 각각 담아 응답한다
            final List<FileInfoDto> fileInfoDtos = new ArrayList<>();
            fileDto.getFiles().forEach(multipartFile -> {
                String uuid = UUID.randomUUID().toString();
                String originalFileName = multipartFile.getOriginalFilename();

                // D:\\upload\\991bb035-7d98-4b3f-99c2-ae59f6ea313f_lenna1.png
                Path filePath = Paths.get(uploadDirPath, uuid + "_" + originalFileName);
                boolean isImage = false;

                try {
                    // 파일을 서버에 저장
                    multipartFile.transferTo(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 파일 정보를 FileIntoDtos에 저장
                fileInfoDtos.add(FileInfoDto.builder()
                        .uuid(uuid)
                        .fileName(originalFileName)
                        .isImage(isImage)
                        .build()
                );
            });
            return fileInfoDtos;
        }
        return null;
    }
}
