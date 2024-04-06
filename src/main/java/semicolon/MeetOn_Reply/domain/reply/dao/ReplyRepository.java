package semicolon.MeetOn_Reply.domain.reply.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import semicolon.MeetOn_Reply.domain.reply.domain.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Page<Reply> findAllByBoardId(Long boardId);
}
