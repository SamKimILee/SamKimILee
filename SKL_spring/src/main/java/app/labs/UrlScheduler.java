package app.labs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//@Configuration
@Component
@EnableScheduling 
@RequiredArgsConstructor
@Slf4j
public class UrlScheduler {
	
	private final RestTemplate restTemplate;
	
//	@Autowired
//	ScheduleController scheduleController;
	
    @Scheduled(fixedRate = 86400000)  // 10000ms = 10초, 86400000 : 1일
    public void callUrl() {
    	log.info("test scheduler1");
//    	scheduleController.scheduledMemberUpdate();
    	String url = "http://localhost/scheduled/memberupdate";
        String response = restTemplate.getForObject(url, String.class);
    	log.info("update done");
    }
}
