package org.demo.board.board.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.RequiredArgsConstructor;
import org.demo.board.board.domain.Board;
import org.demo.board.board.domain.QBoard;
import org.demo.board.board.domain.QReply;
import org.demo.board.board.dto.BoardImageDto;
import org.demo.board.board.dto.BoardListDto;
import org.demo.board.board.dto.BoardListReplyCountDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class);
    }

    // List<Tuple>을 이용한 방식은 Projections를 이용하는 방식보다 번거롭지만 코드로 커스터마이징 할 수 있다
    // DTO 와 Domain 간의 변환은 ModelMapper 또는 Projections를 사용한다
    // 그러나 Board의 Set과 같이 중첩된 구조를 처리할 경우, 직접 튜플(tuple)을 이용해 DTO로 변환하는 것이 편하다
    // searchAll()은 튜플을 DTO로 변환한다
    @Override
    public Page<BoardListDto> searchAll(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        // JPQL
        // select * from board
        JPQLQuery<Board> query = from(board);

        // Left Outer Join
        // select * from board left outer join reply on board.id = reply.board_id
        query.leftJoin(reply).on(reply.board.eq(board));
        // 조인 처리 후, 게시물 당 처리가 필요하므로 groupBy() 적용
        query.groupBy(board);

        // 검색조건과 키워드가 있는 경우
        // where title like '%keyword%' or content like '%keyword%' or writer like '%keyword%'
        if ((types != null && types.length > 0) && keyword != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }
            query.where(booleanBuilder);
        }

        // 페이지네이션
        this.getQuerydsl().applyPagination(pageable, query);

        // Tuple → DTO
        JPQLQuery<Tuple> tupleJPQLQuery = query.select(board, reply.countDistinct());
        List<Tuple> tuples = tupleJPQLQuery.fetch();
        List<BoardListDto> boards = tuples.stream().map(tuple -> {
            Board tempBoard = tuple.get(board);
            long replyCount = tuple.get(1, Long.class);

            BoardListDto boardListDto = BoardListDto.builder()
                    .id(tempBoard.getId())
                    .title(tempBoard.getTitle())
                    .writer(tempBoard.getWriter())
                    .content(tempBoard.getContent())
                    .regDate(tempBoard.getRegDate())
                    .replyCount(replyCount)
                    .build();

            // BoardImage를 BoardImageDto로 처리
            List<BoardImageDto> imageDtos = tempBoard.getBoardImages().stream()
                    .sorted()
                    .map(boardImage -> BoardImageDto.builder()
                            .uuid(boardImage.getUuid())
                            .fileName(boardImage.getFileName())
                            .fileIndex(boardImage.getFileIndex())
                            .build()
                    )
                    .collect(Collectors.toList());

            // 처리된 BoardImageDto를 BoardListDto에 추가
            boardListDto.setBoardImageDtos(imageDtos);
            return boardListDto;
        }).collect(Collectors.toList());

        // 목록 개수
        long count = query.fetchCount();

        return new PageImpl<>(boards, pageable, count);
    }
}
