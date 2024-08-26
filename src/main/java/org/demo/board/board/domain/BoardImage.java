package org.demo.board.board.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString(exclude = "board")
public class BoardImage implements Comparable<BoardImage> {
    // Comparable 인터페이스는 @OneToMany 처리에서 순번에 맞게 정렬하기 위함

    @Id
    @Column(name = "board_image_id")
    private String uuid;
    @Column(name = "file_index")
    private int fileIndex;
    @Column(name = "file_name")
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    // Comparable 인터페이스의 compareTo() 오버라이딩
    @Override
    public int compareTo(BoardImage boardImage) {
        return this.fileIndex - boardImage.getFileIndex();
    }
}
