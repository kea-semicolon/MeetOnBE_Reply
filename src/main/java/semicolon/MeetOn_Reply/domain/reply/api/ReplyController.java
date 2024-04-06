package semicolon.MeetOn_Reply.domain.reply.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import semicolon.MeetOn_Reply.domain.reply.application.ReplyService;

import static semicolon.MeetOn_Reply.domain.reply.dto.ReplyDto.*;

@Slf4j
@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    /**
     * 댓글 작성
     * @param boardId
     * @param replySaveRequestDto
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<String> saveReply(@RequestParam Long boardId,
                                            @RequestBody ReplySaveRequestDto replySaveRequestDto,
                                            HttpServletRequest request) {
        Long replyId = replyService.saveReply(boardId, replySaveRequestDto, request);
        return ResponseEntity.ok(replyId + " " + "Created");
    }

    @GetMapping
    public ResponseEntity<Page<ReplyInfoResponseDto>> replyList(@RequestParam Long boardId,
                                                                Pageable pageable,
                                                                HttpServletRequest request) {
        Page<ReplyInfoResponseDto> replyList = replyService.getReplyList(boardId, pageable, request);
        return ResponseEntity.ok(replyList);
    }
}
