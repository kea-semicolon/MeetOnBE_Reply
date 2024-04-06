package semicolon.MeetOn_Reply.domain.reply.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyBoardService {

    private final WebClient webClient;

    public Boolean BoardExists(Long boardId, String accessToken) {
        String uri = UriComponentsBuilder
                .fromUriString("http://localhost:8000/board/reply/exist")
                .queryParam("boardId", boardId)
                .toUriString();
        return webClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

    public List<ReplyMemberDot>
}
