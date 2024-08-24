package org.demo.board.board.service;

import lombok.RequiredArgsConstructor;
import org.demo.board.board.domain.Board;
import org.demo.board.board.dto.BoardDto;
import org.demo.board.board.repository.BoardRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public Long addBoard(BoardDto boardDto) {
        // DTO → Entity
        Board board = Board.builder()
                .id(boardDto.getId())
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .writer(boardDto.getWriter())
                .build();
        return boardRepository.save(board).getId();
    }
}
