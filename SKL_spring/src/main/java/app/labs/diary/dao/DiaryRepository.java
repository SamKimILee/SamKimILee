package app.labs.diary.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import app.labs.diary.model.Diary;

@Mapper
public interface DiaryRepository {
	int getDiaryCount();
	int getDiaryCount(@Param("memberId") String memberId);
	List <Diary> getDiaryList(@Param("memberId") String memberId);
	
	List <Diary> getDiaryListByDate(@Param("memberId") String memberId, @Param("diaryDate") String diaryDate);
	// 📌 특정 연도/월의 일기 목록 조회 (💡 새롭게 추가!)
    List<Diary> getDiaryListByMonth(@Param("memberId") String memberId, @Param("yearMonth") String yearMonth);
	
	int createDiaryId();
	Diary getDiaryInfo(int diaryId);
	void insertDiary(Diary diary);
	void updateDiary(Diary diary);
	int deleteDiary(@Param("diaryId") int diaryId);
	
	
}
