package org.demo.board.board.service;

import lombok.RequiredArgsConstructor;
import org.demo.board.board.domain.Reply;
import org.demo.board.board.dto.PageRequestDto;
import org.demo.board.board.dto.PageResponseDto;
import org.demo.board.board.dto.ReplyDto;
import org.demo.board.board.repository.ReplyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long register(ReplyDto replyDto) {
        Reply reply = modelMapper.map(replyDto, Reply.class);
        return replyRepository.save(reply).getId();
    }

    @Override
    public PageResponseDto<ReplyDto> getReplies(Long boardId, PageRequestDto pageRequestDto) {
        Pageable pageable = PageRequest.of(
                pageRequestDto.getPage() <= 0 ? 0 : pageRequestDto.getPage() - 1,
                pageRequestDto.getSize(),
                Sort.by("id").ascending()
        );
        Page<Reply> replies = replyRepository.listOfBoard(boardId, pageable);

        // ReplyDto로 변환
        List<ReplyDto> replyDtos = replies.getContent().stream()
                .map(reply -> modelMapper.map(reply, ReplyDto.class))
                .collect(Collectors.toList());

        return PageResponseDto.<ReplyDto>withAll()
                .pageRequestDto(pageRequestDto)
                .dtoList(replyDtos)
                .total((int) replies.getTotalElements())
                .build();
    }
}
