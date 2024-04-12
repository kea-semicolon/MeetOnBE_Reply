package semicolon.MeetOn_Reply.domain.reply.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    @Operation(description = "댓글 작성")
    @PostMapping
    public ResponseEntity<String> saveReply(@RequestParam Long boardId,
                                            @RequestBody ReplySaveRequestDto replySaveRequestDto,
                                            HttpServletRequest request) {
        Long replyId = replyService.saveReply(boardId, replySaveRequestDto, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(replyId + " " + "Created");
    }

    /**
     * 댓글 페이징 목록
     * @param boardId
     * @param pageable
     * @param request
     * @return
     */
    @Operation(description = "댓글 리스트(페이징)")
    @GetMapping
    public ResponseEntity<Page<ReplyInfoResponseDto>> replyList(@RequestParam Long boardId,
                                                                Pageable pageable,
                                                                HttpServletRequest request) {
        Page<ReplyInfoResponseDto> replyList = replyService.getReplyList(boardId, pageable, request);
        return ResponseEntity.ok(replyList);
    }

    /**
     * 댓글 삭제
     * @param replyId
     * @return
     */
    @Operation(description = "댓글 삭제")
    @DeleteMapping
    public ResponseEntity<String> deleteReply(@RequestParam Long replyId) {
        replyService.deleteReply(replyId);
        return ResponseEntity.ok("success");
    }
}
