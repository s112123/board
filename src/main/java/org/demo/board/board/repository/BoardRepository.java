package org.demo.board.board.repository;

import org.demo.board.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

    // @EntityGraph에는 attributePaths라는 속성을 이용해서 같이 로딩해야 하는 속성을 명시할 수 있다
    @EntityGraph(attributePaths = {"boardImages"})
    @Query("select b from Board b where b.id = :id")
    Optional<Board> findByWithImages(@Param("id") Long id);
}
