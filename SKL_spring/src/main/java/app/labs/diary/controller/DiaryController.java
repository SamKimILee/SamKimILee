package app.labs.diary.controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import app.labs.attach.model.Attach;
import app.labs.attach.service.AttachService;
import app.labs.diary.model.Diary;
import app.labs.diary.service.DiaryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class DiaryController {
	
	@Autowired
	DiaryService diaryService;
	
	@Autowired
	AttachService attachService;
		
	@GetMapping("/diary/test")
	public String home(Model model,HttpServletRequest request) {
		model.addAttribute("serverTime", "서버시간");
		
		return "thymeleaf/diary/home";
	}
	
	@GetMapping("/diary/list")
	public String getAllDiary(Model model, HttpSession session) {
		String memberId = (String) session.getAttribute("memberid");

	    // 🔹 로그인 상태 확인 (세션에 memberid가 없는 경우 로그인 페이지로 리다이렉트)
	    if (memberId == null) {
	        return "redirect:/login"; // 로그인 페이지로 이동
	    }
		
		List<Diary> diaryList = diaryService.getDiaryList(memberId);
    	
		model.addAttribute("diaryList", diaryList);
//		log.info("memberId" + memberId);
		return "thymeleaf/diary/list";
	}
	
	@GetMapping("/diary/{diaryId}")
	public String getDiaryInfo(@PathVariable("diaryId") int diaryId, Model model) {
		
		Diary diary = diaryService.getDiaryInfo(diaryId);
		model.addAttribute("diary", diary);
		
		Attach attach = attachService.getAttachFile(diaryId);
		model.addAttribute("attach", attach);
		
		log.info("diary id: " + diaryId + " attach info: " + attach );
				
	    return "thymeleaf/diary/view";
	}

	
	@GetMapping("/diary/insert")
	public String insertDiary(Model model, RedirectAttributes redirectAttributes) {
    	
		model.addAttribute("diary", new Diary());
		
		return "thymeleaf/diary/diaryform";
	}
	
	@PostMapping("/diary/insert")
	public String insertDiary(@ModelAttribute Diary diary, HttpServletRequest request,
	                          @RequestParam(value= "file", required = false) MultipartFile file,
	                          RedirectAttributes redirectAttributes) {
	    HttpSession session = request.getSession();
	    String memberId = (String) session.getAttribute("memberid");

	    try {
	        diary.setMemberId(memberId);
	        diaryService.insertDiary(diary);

	        int diaryId = diary.getDiaryId(); // ✅ 자동 증가된 diaryId 가져오기
	        log.info("새로 생성된 diaryId: " + diaryId); // 🔥 로그 추가

	       if(file != null && !file.isEmpty()) {
	    	   log.info("파일명: " + file.getOriginalFilename());
	    	   
	    	   Attach attach = new Attach();
	    	   attach.setDiaryId(diaryId);
	    	   
	    	   String attachName = file.getOriginalFilename();
	    	   attach.setAttachName(file.getOriginalFilename());
	    	   
	    	   long attachSize = file.getSize();
	    	   attach.setAttachSize(attachSize);
	    	   
	    	   String attachDir = "C:/labs_python/SamkimILee/SKL_spring/src/main/resources/static/attach/" + attachName;
	    	   file.transferTo(new File(attachDir));
	    	   attach.setAttachUrl(attachDir);
	    	   
	    	   attachService.insertAttach(attach);
	       }
	        
	        redirectAttributes.addFlashAttribute("message", "일기가 등록되었습니다.");
	        log.info("일기 등록 성공");
	        
	    } catch (Exception ex) {
	    		ex.printStackTrace();
	    		log.error("일기 등록 오류: ", ex.getMessage());
	    }
	    return "redirect:/diary/list";
	}

	@GetMapping("/diary/update")
	public String updateDiary(@RequestParam("diaryId") int diaryId, Model model) {
	    Diary diary = diaryService.getDiaryInfo(diaryId);
	    model.addAttribute("diary", diary);
	    model.addAttribute("update", true);

	    Attach attach = attachService.getAttachFile(diaryId);
	    model.addAttribute("attach", attach); // 🔥 첨부파일 모델 추가

	    return "thymeleaf/diary/diaryform";
	}
	
	@PostMapping("/diary/update")
	public String updateDiary(@ModelAttribute Diary diary, RedirectAttributes redirectAttributes, 
		                      HttpServletRequest request, @RequestParam(value= "file", required = false) MultipartFile file) {  
		try {
		    log.info("수정 요청된 Diary: " + diary); // ✅ 수정 요청된 Diary 로그 확인
		    
		    diaryService.updateDiary(diary);
		    
		    if(file != null && !file.isEmpty()) {
		    	   
		    	   Attach attach = new Attach();
		    	   attach.setDiaryId(diary.getDiaryId());
		    	   
		    	   String attachName = file.getOriginalFilename();
		    	   attach.setAttachName(attachName);
		    	   
		    	   String attachDir = "C:/labs_python/SamkimILee/SKL_spring/src/main/resources/static/attach/" + attachName;
		    	   file.transferTo(new File(attachDir));
		    	   attach.setAttachUrl(attachDir);
	    		   
	    		   long attachSize = file.getSize();
		    	   attach.setAttachSize(attachSize);
		    	   
		    	   // 기본 첨부파일 확인
		    	   Attach existingAttach = attachService.getAttachFile(diary.getDiaryId());
		    	   
		    	   if(existingAttach != null) {
		    		   attachService.updateAttach(attach);
		    	   } else {
		    		   			    	   
		    		   attachService.insertAttach(attach);
		    	   }

		       }

		    redirectAttributes.addFlashAttribute("message", "일기가 수정되었습니다.");
		    log.info("일기 수정 성공");

		} catch (Exception ex) {
		    ex.printStackTrace();
		    log.error("일기 수정 오류: ", ex.getMessage());
		}
		return "redirect:/diary/list";
	}


	
	@GetMapping("diary/delete")
	public String deleteDiary(@RequestParam("diaryId") int diaryId, HttpServletRequest request,
							  RedirectAttributes redirectAttributes) {
		
		HttpSession session = request.getSession();
		String sessionId = (String) session.getAttribute("memberid");
	
		Diary diary = diaryService.getDiaryInfo(diaryId);
		String memberId = diary.getMemberId();
		
		if(sessionId.equals(memberId)) {
			diaryService.deleteDiary(diaryId);
		}
		
		redirectAttributes.addFlashAttribute("message", "일기가 삭제되었습니다.");
		
		return "redirect:/diary/list";
	}


	
}
