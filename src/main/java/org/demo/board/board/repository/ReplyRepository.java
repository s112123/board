package org.demo.board.board.repository;

import org.demo.board.board.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    // 특정 게시물에서 댓글 목록들이 페이징 처리를 할 수 있도록 @Query를 이용해서 작성
    @Query(value = "select r from Reply r where r.board.id = :id")
    Page<Reply> listOfBoard(@Param("id") Long boardId, Pageable pageable);
}
