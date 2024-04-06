package semicolon.MeetOn_Reply.domain.reply.application;

import com.netflix.discovery.converters.Auto;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static semicolon.MeetOn_Reply.domain.reply.dto.ReplyDto.*;

@Slf4j
@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
class ReplyServiceTest {

    @MockBean
    ReplyRepository replyRepository;

    @Autowired
    ReplyService replyService;

    @MockBean
    ReplyBoardService replyBoardService;

    @MockBean
    ReplyMemberService replyMemberService;

    @MockBean
    CookieUtil cookieUtil;

    MockHttpServletRequest request;
    MockHttpServletResponse response;

    Pageable pageable;

    @BeforeEach
    void 세팅() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer test-token");
        createSetCookie("channelId", String.valueOf(7L));
        createSetCookie("memberId", String.valueOf(1L));
        pageable = PageRequest.of(0, 10);
        when(cookieUtil.getCookieValue("memberId", request)).thenReturn("1");
    }

    @Test
    void 댓글_작성() {
        //given
        ReplySaveRequestDto replySaveRequestDto = new ReplySaveRequestDto("testing");
        Reply savedReply = new Reply(1L, replySaveRequestDto.getContent(), 1L, 1L);

        //when
        when(replyBoardService.boardExists(1L, "Bearer test-token")).thenReturn(true);
        when(replyRepository.save(any(Reply.class))).thenReturn(savedReply);
        Long result = replyService.saveReply(1L, replySaveRequestDto, request);

        //then
        assertThat(result).isEqualTo(savedReply.getId());
    }

    @Test
    void 댓글_목록() {
        //given
        List<Reply> replies = List.of(
                new Reply(1L, "Content 1", 1L, 1L), // Assume Reply constructor and fields
                new Reply(2L, "Content 2", 1L, 2L)
        );
        List<ReplyMemberDto> memberInfoLists = List.of(
                new ReplyMemberDto(1L, "test1"),
                new ReplyMemberDto(2L, "test2")
        );
        Page<Reply> replyPage = new PageImpl<>(replies, pageable, replies.size());

        //when
        when(replyBoardService.boardExists(1L, "Bearer test-token")).thenReturn(true);
        when(replyRepository.findAllByBoardId(1L, pageable)).thenReturn(replyPage);
        when(replyMemberService.getUserInfoList(List.of(1L, 2L), "Bearer test-token")).thenReturn(memberInfoLists);
        Map<Long, String> memberIdToUsernameMap = memberInfoLists.stream()
                .collect(Collectors.toMap(ReplyMemberDto::getId, ReplyMemberDto::getUsername));
        List<ReplyInfoResponseDto> result = replies
                .stream()
                .map(reply -> ReplyInfoResponseDto.builder()
                        .replyId(reply.getId())
                        .userId(reply.getMemberId())
                        .username(memberIdToUsernameMap.get(reply.getMemberId()))
                        .content(reply.getContent())
                        .createdDate(reply.getCreatedAt())
                        .build()
                ).toList();
        Page<ReplyInfoResponseDto> resultPage = replyService.getReplyList(1L, pageable, request);

        //then
        assertThat(result.size()).isEqualTo(resultPage.getContent().size());
    }

    @Test
    void 댓글_삭제() {
        //given
        Long replyId = 1L;
        Reply reply = new Reply(replyId, "test", 1L, 1L);

        //when
        when(replyRepository.findById(replyId)).thenReturn(Optional.of(reply));
        replyService.deleteReply(replyId);

        //then
        verify(replyRepository).findById(replyId);
        verify(replyRepository).delete(reply);
    }

    private void createSetCookie(String name, String value) {
        MockCookie mockCookie = new MockCookie(name, value);
        mockCookie.setPath("/");
        mockCookie.setHttpOnly(true);
        response.addCookie(mockCookie);
        Cookie[] cookies = response.getCookies();
        request.setCookies(cookies);
    }
}