package semicolon.MeetOn_Reply;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import semicolon.MeetOn_Reply.global.logtrace.LogTrace;
import semicolon.MeetOn_Reply.global.logtrace.ThreadLocalLogTrace;

@EnableJpaAuditing
@EnableDiscoveryClient
@SpringBootApplication
public class MeetOnReplyApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeetOnReplyApplication.class, args);
	}

	@Bean
	public LogTrace logTrace() {
		return new ThreadLocalLogTrace();
	}
}
