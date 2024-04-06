package semicolon.MeetOn_Reply.domain.reply.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.MeetOn_Reply.domain.reply.dao.ReplyRepository;
import semicolon.MeetOn_Reply.domain.reply.domain.Reply;
import semicolon.MeetOn_Reply.domain.reply.dto.ReplyDto;
import semicolon.MeetOn_Reply.global.exception.BusinessLogicException;
import semicolon.MeetOn_Reply.global.exception.code.ExceptionCode;
import semicolon.MeetOn_Reply.global.util.CookieUtil;

import java.util.ArrayList;
import java.util.List;

import static semicolon.MeetOn_Reply.domain.reply.dto.ReplyDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ReplyBoardService replyBoardService;
    private final CookieUtil cookieUtil;

    @Transactional
    public Long saveReply(Long boardId, ReplySaveRequestDto replySaveRequestDto, HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        Long memberId = Long.valueOf(cookieUtil.getCookieValue("memberId", request));
        if (!replyBoardService.BoardExists(boardId, accessToken)) {
           throw new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND);
        }

        Reply reply = Reply.toReply(replySaveRequestDto, memberId, boardId);
        Reply save = replyRepository.save(reply);
        return save.getId();
    }

    public Page<ReplyInfoResponseDto> getReplyList(Long boardId, HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if(!replyBoardService.BoardExists(boardId, accessToken)){
            throw new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND);
        }

        Page<Reply> replyPage = replyRepository.findAllByBoardId(boardId);
        List<Reply> replyList = replyPage.getContent();
        List<Long> memberidList = replyList.stream().map(Reply::getMemberId)
                .toList();

    }
}

