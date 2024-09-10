package org.demo.board.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.board.board.domain.Board;
import org.demo.board.board.dto.*;
import org.demo.board.board.service.BoardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @Value("${file.upload.dir.path}")
    private String uploadDir;

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
        PageResponseDto<BoardListDto> boards = boardService.getBoards(pageRequestDto);
        model.addAttribute("boards", boards);
        return "board/list";
    }

    // 수정 페이지
    // 경로에 따라 조회 페이지로 이동할 수 있으므로 void로 처리해야 한다
    @GetMapping({"/read", "/modify"})
    public void read(@RequestParam("id") Long id, PageRequestDto pageRequestDto, Model model) {
        BoardDto board = boardService.getBoard(id);
        model.addAttribute("board", board);
    }

    // 수정하기
    @PostMapping("/modify")
    public String modify(
            @Valid BoardDto boardDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            PageRequestDto pageRequestDto
    ) {
        // 유효성 검사
        if (bindingResult.hasErrors()) {
            // 수정시, 문제가 발생하면 기존의 모든 조건을 원래대로 붙여서 /board/modify 경로로 이동한다
            String link = pageRequestDto.getLink();
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("bno", boardDto.getId());
            return "redirect:/board/modify?" + link;
        }

        // 수정이 완료되면 아무런 검색이나 페이징 조건없이 단순히 /board/read 경로로 이동
        // 목록에서 검색하여 수정한 경우, 수정된 내용과 검색조건이 일치하지 않을 수 있다
        // 그러므로 조회 페이지를 보여주는 것이 안정적이다
        boardService.modify(boardDto);
        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("id", boardDto.getId());
        return "redirect:/board/read";
    }

    // 삭제하기
    // 게시물 삭제에는 게시물에 있는 댓글을 먼저 삭제해야만 한다 (참조 무결성)
    // ReplyRepository에 void deleteByBoard_Bno(Long bno); 삽입한다
    // 게시물 삭제: replyRepository.deleteByBoard_bno(bno); → boardRepository.deleteById(bno);
    // 다만, 이런 경우 다른 사용자가 만든 데이터를 삭제하는 것은 문제가 될 수 있으므로 주의할 필요가 있다
    // 이번 프로젝트에서는 댓글이 달려있지 않은 글만 삭제한다
    // 댓글이 있는 글을 삭제하면 제약조건에 의해 {"msg":"constraint fails","time":"1698630245271"} 에러가 발생한다
    @PostMapping("/remove")
    public String remove(BoardDto boardDto) {
        Long id = boardDto.getId();
        boardService.remove(id);

        // 게시물이 삭제되었다면 첨부파일도 삭제
        List<String> fileNames = boardDto.getFileNames();
        if (fileNames != null && fileNames.size() > 0) {
            removeFiles(fileNames);
        }

        // 삭제 후, 목록으로 이동
        return "redirect:/board/list";
    }

    // 게시물 삭제 시, 첨부파일 삭제
    public void removeFiles(List<String> fileNames) {
        for (String fileName : fileNames) {
            Resource resource = new FileSystemResource(uploadDir + File.separator + fileName);
            String resourceName = resource.getFilename();

            try {
                String contentType = Files.probeContentType(resource.getFile().toPath());
                resource.getFile().delete();

                // 썸네일 파일 삭제
                if (contentType.startsWith("image")) {
                    File thumbnailFile = new File(uploadDir + File.separator + "s_" + fileName);
                    thumbnailFile.delete();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }
}
