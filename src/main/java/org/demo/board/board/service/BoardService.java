package org.demo.board.board.service;

import org.demo.board.board.domain.Board;
import org.demo.board.board.domain.BoardImage;
import org.demo.board.board.dto.BoardDto;

public interface BoardService {

    // 등록하기
    Long addBoard(BoardDto boardDto);

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
}
