package semicolon.MeetOn_Reply.domain.reply.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import semicolon.MeetOn_Reply.domain.reply.dto.ReplyDto;

import static semicolon.MeetOn_Reply.domain.reply.dto.ReplyDto.*;

@Getter
@Entity
@NoArgsConstructor
public class Reply {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private Long boardId;
    private Long memberId;

    @Builder
    public Reply(Long id, String content, Long boardId, Long memberId) {
        this.id = id;
        this.content = content;
        this.boardId = boardId;
        this.memberId = memberId;
    }

    public static Reply toReply(ReplySaveRequestDto replySaveRequestDto, Long memberId, Long boardId) {
        return Reply
                .builder()
                .content(replySaveRequestDto.getContent())
                .memberId(memberId)
                .boardId(boardId)
                .build();
    }
}
