package semicolon.MeetOn_Reply.domain.reply.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ReplyDto {

    @Getter
    @NoArgsConstructor
    public static class ReplySaveRequestDto {
        private String content;

        @Builder
        public ReplySaveRequestDto(String content) {
            this.content = content;
        }
    }

    @Getter
    @Builder
    public static class ReplyInfoResponseDto {
        private Long replyId;
        private Long userId;
        private String username;
        private String content;
        private LocalDateTime createdDate;

//        public static ReplyInfoResponseDto toReplyInfoResponseDto() {
//
//        }
    }
}
