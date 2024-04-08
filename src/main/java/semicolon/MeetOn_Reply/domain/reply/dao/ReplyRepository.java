package semicolon.MeetOn_Reply.domain.reply.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import semicolon.MeetOn_Reply.domain.reply.domain.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Page<Reply> findAllByBoardId(Long boardId, Pageable pageable);

    @Modifying
    @Query("delete from Reply r where r.boardId = :boardId")
    int deleteRepliesByBoardId(Long boardId);
}
