package org.demo.board.board.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.RequiredArgsConstructor;
import org.demo.board.board.domain.Board;
import org.demo.board.board.domain.QBoard;
import org.demo.board.board.domain.QReply;
import org.demo.board.board.dto.BoardListReplyCountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class);
    }

    // 게시물 목록에 댓글 수 표시
    // 현재 Reply (*) - (1) Board 단방향 참조를 하고 있으므로 join 으로 참조한다
    // 게시물과 댓글의 경우, 한쪽에만 데이터가 존재하는 상황이 발생할 수 있다
    // 예를 들어, 특정 게시물은 댓글이 없는 경우가 발생하므로 inner join 대신 left (outer) join 을 통해 처리
    @Override
    public Page<BoardListReplyCountDto> searchAll(String[] types, String keyword, Pageable pageable) {
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

        // where id > 0
        query.where(board.id.gt(0L));

        // JPA에서는 Projections 이라는 JPQL의 결과를 바로 DTO로 처리하는 기능을 제공하는 것이 있다
        // Querydsl도 마찬가지로 이러한 기능을 제공한다
        // 목록화면에서 필요한 쿼리의 결과를 Projections.bean()을 이용해서 한번에 DTO로 처리할 수 있다
        // 이를 이용하려면 JPQLQuery 객체의 select()를 이용해야 한다
        JPQLQuery<BoardListReplyCountDto> dtoQuery = query.select(
                Projections.bean(
                        BoardListReplyCountDto.class,
                        board.id,
                        board.title,
                        board.writer,
                        board.regDate,
                        reply.count().as("replyCount")
                )
        );

        // 페이지네이션
        this.getQuerydsl().applyPagination(pageable, dtoQuery);

        // 목록조회
        List<BoardListReplyCountDto> boards = dtoQuery.fetch();
        long count = dtoQuery.fetchCount();

        return new PageImpl<>(boards, pageable, count);
    }
}
