package org.demo.board.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDto<E> {

    private int page;
    private int size;
    private int total;
    private int start;
    private int end;
    private boolean prev;
    private boolean next;
    private List<E> boardDtos;

    // 이 생성자로 생성하는 함수를 withAll()로 설정
    @Builder(builderMethodName = "withAll")
    public PageResponseDto(PageRequestDto pageRequestDto, List<E> boardDtos, int total) {
        if (total <= 0) {
            return;
        }

        this.page = pageRequestDto.getPage();
        this.size = pageRequestDto.getSize();
        this.total = total;
        this.boardDtos = boardDtos;
        this.end = (int)(Math.ceil(this.page / 10.0 )) *  10;
        this.start = this.end - 9;
        int last = (int)(Math.ceil((total / (double)size)));
        this.end =  end > last ? last: end;
        this.prev = this.start > 1;
        this.next =  total > this.end * this.size;
    }
}
