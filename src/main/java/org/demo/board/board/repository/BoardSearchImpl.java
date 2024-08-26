package org.demo.board.board.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.RequiredArgsConstructor;
import org.demo.board.board.domain.Board;
import org.demo.board.board.domain.QBoard;
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

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;

        // JPQL
        // select * from board
        JPQLQuery<Board> query = from(board);

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

        // 페이지네이션
        this.getQuerydsl().applyPagination(pageable, query);

        // 목록조회
        List<Board> boards = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(boards, pageable, count);
    }
}
