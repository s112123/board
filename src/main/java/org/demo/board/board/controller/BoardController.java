package org.demo.board.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {

    // 게시판 등록 페이지
    @GetMapping("/add")
    public void addBoardForm() {
    }
}
