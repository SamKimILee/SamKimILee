package app.labs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ScheduleController {
	
	@Autowired
    private ResourceLoader resourceLoader;
	
	@GetMapping("/scheduled/memberupdate")
    public String scheduledMemberUpdate(Model model) throws IOException {
		log.info("scheduledMemberUpdate 메서드 실행");
		Resource resource = resourceLoader.getResource("classpath:query.properties");

        if (resource.exists()) {
            String content = new String(Files.readAllBytes(Paths.get(resource.getURI())));
//            log.info(content);
            model.addAttribute("fileContent", content);
        } else {
            model.addAttribute("fileContent", "파일을 찾을 수 없습니다.");
        }
		return "scheduler";
    }
}
