package org.demo.board.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.board.board.domain.Board;
import org.demo.board.board.dto.*;
import org.demo.board.board.repository.BoardRepository;
import org.demo.board.board.repository.BoardSearch;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public Long addBoard(BoardDto boardDto) {
        // DTO → Entity
        org.demo.board.board.domain.Board board = boardDtoToBoard(boardDto);
        return boardRepository.save(board).getId();
    }

    @Override
    public PageResponseDto<BoardListDto> getBoards(PageRequestDto pageRequestDto) {
        // 검색조건
        String[] types = pageRequestDto.getTypes();
        String keyword = pageRequestDto.getKeyword();

        // 검색조건 및 페이지 처리된 목록
        Pageable pageable = pageRequestDto.getPageable("id");
        Page<BoardListDto> boardDtos = boardRepository.searchAll(types, keyword, pageable);

        // BoardListDto → PageResponseDto<E>
        // withAll(): PageResponseDto 생성자의 매개변수에 인수를 할당한다
        return PageResponseDto.<BoardListDto>withAll()
                .pageRequestDto(pageRequestDto)
                .dtoList(boardDtos.getContent())
                .total((int) boardDtos.getTotalElements())
                .build();
    }

    @Override
    public BoardDto getBoard(Long id) {
        // Entity
        Board board = boardRepository.findByWithImages(id).orElseThrow();
        // Entity → DTO
        BoardDto boardDto = boardToBoardDto(board);
        return boardDto;
    }
}
