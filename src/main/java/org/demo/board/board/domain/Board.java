package org.demo.board.board.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor
@Getter
@Builder
@ToString
public class Board {

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
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<BoardImage> boardImages = new HashSet<>();

    // 양방향 관계인 경우, 참조 객체의 관계가 서로 일치하도록 해야 한다
    // Board 엔티티를 저장할 때, boardImages에 BoardImage를 저장하고 BoardImage 엔티티에 Board를 저장한다
    public void addBoardImage(BoardImage image) {
        BoardImage boardImage = BoardImage.builder()
                .fileIndex(image.getFileIndex())
                .fileName(image.getFileName())
                .board(this)
                .build();
        boardImages.add(boardImage);
    }
}
