package app.labs;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//@Configuration
@Component
@EnableScheduling 
@RequiredArgsConstructor
@Slf4j
public class UrlScheduler {
	
	private final WebClient webClient;
	
//    @Scheduled(fixedRate = 86400000)  // 10000ms = 10초, 86400000 : 1일
    @Scheduled(cron = "0 0 0 * * *")
    public void callUrl() {
    	log.info("test UrlScheduler");
    	String url = "http://localhost/scheduled/memberupdate";
    	
    	webClient.get()
		        .uri(url)
		        .retrieve()
		        .bodyToMono(String.class)
		        .subscribe(response -> log.info("✔️ WebClient 호출 결과: {}", response),
		                   error -> log.error("❌ 호출 실패", error));
    	
    	log.info("update done");
    }
}
