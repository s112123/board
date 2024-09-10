package org.demo.board.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.board.board.domain.Board;
import org.demo.board.board.dto.BoardDto;
import org.demo.board.board.dto.BoardListReplyCountDto;
import org.demo.board.board.dto.PageRequestDto;
import org.demo.board.board.dto.PageResponseDto;
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
    public PageResponseDto<BoardListReplyCountDto> getBoards(PageRequestDto pageRequestDto) {
        // 검색조건
        String[] types = pageRequestDto.getTypes();
        String keyword = pageRequestDto.getKeyword();

        // board_id를 기준으로 내림차순 정렬하는 Pageable 생성
        Pageable pageable = pageRequestDto.getPageable("id");
        Page<BoardListReplyCountDto> boardDtos = boardRepository.searchAll(types, keyword, pageable);

        // BoardListReplyCountDto → PageResponseDto<E>
        // withAll(): PageResponseDto 생성자의 매개변수에 인수를 할당한다
        return PageResponseDto.<BoardListReplyCountDto>withAll()
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
