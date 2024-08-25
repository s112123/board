package org.demo.board.board.controller;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.demo.board.board.dto.FileDto;
import org.demo.board.board.dto.FileInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Slf4j
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

                    // 파일이 이미지일 때는 썸네일 파일도 저장
                    if (Files.probeContentType(filePath).startsWith("image")) {
                        isImage = true;

                        // 썸네일 파일 이름은 원래 파일 이름 앞에 "s_" 로 시작하도록 구성
                        File thumbnail = new File(uploadDirPath, "s_" + uuid + "_" + originalFileName);
                        Thumbnailator.createThumbnail(filePath.toFile(), thumbnail, 200, 200);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 파일 정보를 FileIntoDtos에 저장
                fileInfoDtos.add(
                        FileInfoDto.builder()
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

    // 썸네일 이미지 조회
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewThumbnail(@PathVariable("fileName") String fileName) {
        Resource resource = new FileSystemResource(uploadDirPath + File.separator + fileName);
        HttpHeaders headers = new HttpHeaders();

        try {
            // resource.getFile().toPath() → D:\\upload\\s_1a7d89e3-e32b-486a-bcf9-478ca29cd6a2_lenna1.png
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().headers(headers).body(resource);
    }

    // 첨부한 이미지 파일 삭제
    @DeleteMapping("/remove/{fileName}")
    public Map<String, Boolean> removeFile(@PathVariable("fileName") String fileName) {
        Resource resource = new FileSystemResource(uploadDirPath + File.separator + fileName);
        Map<String, Boolean> responseBody = new HashMap<>();
        boolean removed = false;

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            log.info("contentType={}", contentType);
            removed = resource.getFile().delete();

            // 파일이 이미지라서 썸네일이 존재한다면 썸네일도 삭제
            if (contentType.startsWith("image")) {
                File thumbnail = new File(uploadDirPath + File.separator + "s_" + fileName);
                thumbnail.delete();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        responseBody.put("result", removed);
        return responseBody;
    }
}
