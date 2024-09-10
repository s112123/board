package org.demo.board.board.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;
    @Column(length = 500, nullable = false)
    private String title;
    @Column(length = 2000, nullable = false)
    private String content;
    @Column(length = 50, nullable = false)
    private String writer;

    // Board (1) - (*) BoardImage 양방향
    // CascadeType.ALL → Board 엔티티의 상태 변화에 따라 BoardImage 엔티티도 함께 변경
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    @Builder.Default
    private Set<BoardImage> boardImages = new HashSet<>();

    // 양방향 관계인 경우, 참조 객체의 관계가 서로 일치하도록 해야 한다
    // Board 엔티티를 저장할 때, boardImages 변수에 BoardImage를 저장하고 BoardImage 엔티티에 Board를 저장한다
    public void addBoardImage(String uuid, String filename) {
        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileIndex(boardImages.size())
                .fileName(filename)
                .board(this)
                .build();
        boardImages.add(boardImage);
    }
}
