package org.demo.board.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.board.board.dto.PageRequestDto;
import org.demo.board.board.dto.PageResponseDto;
import org.demo.board.board.dto.ReplyDto;
import org.demo.board.board.service.ReplyService;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/replies")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    // 등록하기
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> register(
            @Valid @RequestBody ReplyDto replyDto, BindingResult bindingResult
    ) throws BindException {
        // 유효성 검사 및 리플 등록
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Long id = replyService.register(replyDto);

        // 응답 메세지
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);

        // 메소드 리턴값에 문제가 있다면 @RestControllerAdvice가 처리할 것이므로 정상적인 결과만 리턴
        return map;
    }

    // 목록조회
    @GetMapping("/list/{boardId}")
    public PageResponseDto<ReplyDto> getReplies(
            @PathVariable("boardId") Long boardId, PageRequestDto pageRequestDto
    ) {
        PageResponseDto<ReplyDto> pageResponseDto = replyService.getReplies(boardId, pageRequestDto);
        return pageResponseDto;
    }
}
