package org.demo.board.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
public class BaseEntity {

    // 등록일
    @CreatedDate
    @Column(name = "reg_date", updatable = false)
    private LocalDateTime regDate;

    // 수정일
    @LastModifiedDate
    @Column(name = "mod_date")
    private LocalDateTime modDate;
}
