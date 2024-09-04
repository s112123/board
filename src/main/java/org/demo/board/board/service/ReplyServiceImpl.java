package org.demo.board.board.service;

import lombok.RequiredArgsConstructor;
import org.demo.board.board.domain.Reply;
import org.demo.board.board.dto.ReplyDto;
import org.demo.board.board.repository.ReplyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
}
