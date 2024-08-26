package org.demo.board.board.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageRequestDto {

    // 페이지네이션 관련 시작
    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;

    // 페이지네이션 처리를 위한 Pageable 타입 반환
    // props → 정렬 기준이 되는 항목
    public Pageable getPageable(String... props) {
        return PageRequest.of(this.page - 1, this.size, Sort.by(props).descending());
    }
    // 페이지네이션 관련 끝

    // 검색 관련 시작
    private String type;
    private String keyword;

    // 검색조건을 BoardRepository 에서 String[] 타입의 인수로 받기 때문에 type 문자열을 배열로 반환해준다
    public String[] getTypes() {
        if (type == null || type.isEmpty()) {
            return null;
        }
        return type.split("");
    }

    // 검색 조건과 페이징 조건 등을 문자열로 구성
    // page=1&size=10&type=t&keyword=a
    private String link;
    public String getLink() {
        if (link == null) {
            // 쿼리스트링에 page, size 연결
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("page=" + this.page);
            stringBuilder.append("&size=" + this.size);

            // 쿼리스트링에 type 연결
            if (type != null && type.length() > 0) {
                stringBuilder.append("&type=" + type);
            }

            // 쿼리스트링에 keyword 연결
            if (keyword != null) {
                try {
                    stringBuilder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    log.error(e.getMessage());
                }
            }

            link = stringBuilder.toString();
        }
        return link;
    }
    // 검색 관련 끝
}
