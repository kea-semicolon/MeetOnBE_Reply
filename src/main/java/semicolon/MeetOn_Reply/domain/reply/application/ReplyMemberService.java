package semicolon.MeetOn_Reply.domain.reply.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import semicolon.MeetOn_Reply.domain.reply.dto.ReplyMemberDto;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyMemberService {

    private final WebClient webClient;

    @Value("${app.gateway.url}")
    private String gateway;

    public List<ReplyMemberDto> getUserInfoList(List<Long> userIdList, String accessToken) {
        String uri = UriComponentsBuilder
                .fromUriString(gateway + "/member/reply/infoList")
                .toUriString();

        return webClient
                .post()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .bodyValue(userIdList)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List < ReplyMemberDto >>() {
                })
                .block();
    }
}
