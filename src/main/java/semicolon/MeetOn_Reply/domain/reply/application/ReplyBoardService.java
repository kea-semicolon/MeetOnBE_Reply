package semicolon.MeetOn_Reply.domain.reply.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyBoardService {

    private final WebClient webClient;

    @Value("${app.gateway.url}")
    private String gateway;

    public Boolean boardExists(Long boardId, String accessToken) {
        String uri = UriComponentsBuilder
                .fromUriString(gateway + "/board/reply/exist")
                .queryParam("boardId", boardId)
                .toUriString();
        return webClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }
}
