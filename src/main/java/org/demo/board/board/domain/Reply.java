package org.demo.board.board.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reply", indexes = {@Index(name = "idx_reply_board_id", columnList = "board_id")})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "board")
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;
    private String text;
    private String writer;

    // Reply (*) - (1) Board 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
}
