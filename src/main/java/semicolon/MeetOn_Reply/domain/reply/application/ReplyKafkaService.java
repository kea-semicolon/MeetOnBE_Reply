package semicolon.MeetOn_Reply.domain.reply.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.MeetOn_Reply.domain.reply.dao.ReplyRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyKafkaService {

    private final ReplyRepository replyRepository;
    private final static String BOARD_DELETED_TOPIC = "board_deleted_topic";

    @Transactional
    @KafkaListener(topics = BOARD_DELETED_TOPIC, groupId = "board-group")
    public void deleteByBoardDeleted(String boardIdStr) {
        log.info("Board 삭제 boardId={}", boardIdStr);
        Long boardId = Long.valueOf(boardIdStr);
        int c = replyRepository.deleteRepliesByBoardId(boardId);
        log.info("Reply {}개 삭제 완료", c);
    }
}
