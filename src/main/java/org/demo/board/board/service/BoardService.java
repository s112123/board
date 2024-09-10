package org.demo.board.board.service;

import org.demo.board.board.domain.Board;
import org.demo.board.board.domain.BoardImage;
import org.demo.board.board.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {

    // 등록하기
    Long addBoard(BoardDto boardDto);

    // 목록조회 + 댓글 수 + 이미지
    PageResponseDto<BoardListDto> getBoards(PageRequestDto pageRequestDto);

    // 조회하기
    BoardDto getBoard(Long id);

    // 수정하기
    void modify(BoardDto boardDto);

    // 삭제하기
    void remove(Long id);

    // DTO → Entity (BoardDto → Board)
    // ModelMapper는 단순한 구조의 객체를 다른 타입의 객체로 만드는데 편리하지만 다양한 처리가 힘들다
    // BoardService 인터페이스에서 DTO와 엔티티를 모두 처리하는 경우가 많으므로 default 메소드로 처리
    default Board boardDtoToBoard(BoardDto boardDto) {
        Board board = Board.builder()
                .id(boardDto.getId())
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .writer(boardDto.getWriter())
                .build();

        if (boardDto.getFileNames() != null) {
            // fileName → uuid_filename.확장자
            boardDto.getFileNames().forEach(fileName -> {
                String[] fileNames = fileName.split("_");
                board.addBoardImage(fileNames[0], fileNames[1]);
            });
        }

        return board;
    }

    // Entity → DTO (Board → BoardDto)
    default BoardDto boardToBoardDto(Board board) {
        // DB에서 조회된 Board 객체를 DTO 객체에 저장
        BoardDto boardDto = BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        // BoardDto의 List<String> fileNames에 파일이름 저장
        List<String> fileNames = board.getBoardImages().stream()
                .sorted()
                .map(boardImage -> boardImage.getUuid() + "_" + boardImage.getFileName())
                .collect(Collectors.toList());
        boardDto.setFileNames(fileNames);

        return boardDto;
    }
}
