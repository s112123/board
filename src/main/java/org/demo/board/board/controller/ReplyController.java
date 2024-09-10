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

    // 조회하기
    // 댓글 번호가 없는 번호로 조회가 되면 500에러가 발생한다
    // 서비스 계층에서 조회시 Optional<T>를 이용했고 orElseThrow()를 이용했기 때문에 컨트롤러에 예외가 전달된다
    // NoSuchElementExcpetion 예외를 처리하기 위해 CustomRestAdvice 클래스를 이용해서 예외처리를 추가해준다
    @GetMapping("/{id}")
    public ReplyDto getReply(@PathVariable("id") Long id) {
        ReplyDto replyDto = replyService.read(id);
        return replyDto;
    }

    // 수정하기
    // 수정할 때도 등록과 마찬가지로 JSON 문자열이 전송되므로 이를 처리하도록 @RequestBody를 적용한다
    // REST API 테스트 도구로 테스트하는 경우: {"text": "댓글수정내용"}
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> modify(@PathVariable("id") Long id, @RequestBody ReplyDto replyDto) {
        // 댓글 ID를 일치시킴
        replyDto.setId(id);
        replyService.modify(replyDto);

        Map<String, Long> map = new HashMap<>();
        map.put("id", id);
        return map;
    }
}
