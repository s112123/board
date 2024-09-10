package org.demo.board.board.repository;

import org.demo.board.board.dto.BoardListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {

    // 목록조회 → 페이지네이션 + 검색 + 댓글 수 + 이미지
    // OR 조건 검색: types는 제목(t), 내용(c), 작성자(w)로 구성된다
    Page<BoardListDto> searchAll(String[] types, String keyword, Pageable pageable);
}
