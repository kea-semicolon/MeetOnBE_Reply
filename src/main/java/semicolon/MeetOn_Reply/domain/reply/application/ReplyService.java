package semicolon.MeetOn_Reply.domain.reply.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.MeetOn_Reply.domain.reply.dao.ReplyRepository;
import semicolon.MeetOn_Reply.domain.reply.domain.Reply;
import semicolon.MeetOn_Reply.domain.reply.dto.ReplyDto;
import semicolon.MeetOn_Reply.domain.reply.dto.ReplyMemberDto;
import semicolon.MeetOn_Reply.global.exception.BusinessLogicException;
import semicolon.MeetOn_Reply.global.exception.code.ExceptionCode;
import semicolon.MeetOn_Reply.global.util.CookieUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static semicolon.MeetOn_Reply.domain.reply.dto.ReplyDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ReplyBoardService replyBoardService;
    private final ReplyMemberService replyMemberService;
    private final CookieUtil cookieUtil;

    /**
     * 댓글 작성
     * @param boardId
     * @param replySaveRequestDto
     * @param request
     * @return
     */
    @Transactional
    public Long saveReply(Long boardId, ReplySaveRequestDto replySaveRequestDto, HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        Long memberId = Long.valueOf(cookieUtil.getCookieValue("memberId", request));
        if (!replyBoardService.boardExists(boardId, accessToken)) {
           throw new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND);
        }

        Reply reply = Reply.toReply(replySaveRequestDto, memberId, boardId);
        Reply save = replyRepository.save(reply);
        return save.getId();
    }

    /**
     * 해당 Board에 대한 Reply 페이징 목록
     * @param boardId
     * @param pageable
     * @param request
     * @return
     */
    public Page<ReplyInfoResponseDto> getReplyList(Long boardId, Pageable pageable, HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if(!replyBoardService.boardExists(boardId, accessToken)){
            throw new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND);
        }

        Page<Reply> replyPage = replyRepository.findAllByBoardId(boardId, pageable);
        List<Reply> replyList = replyPage.getContent();
        List<Long> memberidList = replyList.stream().map(Reply::getMemberId)
                .toList();
        List<ReplyMemberDto> memberInfoList = replyMemberService.getUserInfoList(memberidList, accessToken);
        Map<Long, String> memberIdToUsernameMap = memberInfoList.stream()
                .collect(Collectors.toMap(ReplyMemberDto::getId, ReplyMemberDto::getUsername));

        List<ReplyInfoResponseDto> result = replyList
                .stream()
                .map(reply -> ReplyInfoResponseDto.builder()
                        .replyId(reply.getId())
                        .userId(reply.getMemberId())
                        .username(memberIdToUsernameMap.get(reply.getMemberId()))
                        .content(reply.getContent())
                        .createdDate(reply.getCreatedAt())
                        .build()
                ).toList();
        return new PageImpl<>(result, pageable, replyPage.getTotalPages());
    }

    /**
     * 댓글 삭제
     * @param replyId
     */
    @Transactional
    public void deleteReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.REPLY_NOT_FOUND));
        replyRepository.delete(reply);
    }
}

