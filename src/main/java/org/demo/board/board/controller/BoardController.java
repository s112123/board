package org.demo.board.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.board.board.domain.Board;
import org.demo.board.board.dto.BoardDto;
import org.demo.board.board.dto.PageRequestDto;
import org.demo.board.board.dto.PageResponseDto;
import org.demo.board.board.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시판 등록 페이지
    @GetMapping("/add")
    public void addBoardForm() {
    }

    // 게시판 등록
    @PostMapping("/add")
    public String addBoard(
            @Valid BoardDto boardDto, BindingResult bindingResult, RedirectAttributes redirectAttributes
    ) {
        // @Valid 검증을 실패하면 addFlashAttribute()를 통해서 'errors'라는 이름으로 메시지들이 전송됨
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/board/add";
        }

        // list.html 이동할 때, 전달한 'id' 데이터를 모달창으로 처리한다
        Long id = boardService.addBoard(boardDto);
        redirectAttributes.addFlashAttribute("id", id);
        return "redirect:/board/list";
    }

    // 게시판 목록 페이지
    @GetMapping("/list")
    public String listBoard(PageRequestDto pageRequestDto, Model model) {
        PageResponseDto<BoardDto> boards = boardService.getBoards(pageRequestDto);
        model.addAttribute("boards", boards);
        return "board/list";
    }
}
