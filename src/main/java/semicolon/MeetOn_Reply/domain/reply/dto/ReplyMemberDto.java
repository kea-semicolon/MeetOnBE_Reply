package semicolon.MeetOn_Reply.domain.reply.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplyMemberDto {

    private Long id;
    private String username;

    @Builder
    public ReplyMemberDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
