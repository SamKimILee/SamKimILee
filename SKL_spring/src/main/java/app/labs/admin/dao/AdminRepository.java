package app.labs.admin.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import app.labs.register.model.Member;
import app.labs.board.model.Board;
import app.labs.admin.model.Events;

@Mapper
public interface AdminRepository {

    // 관리자 로그인
    Map<String, Object> findById(String id);

    // 회원 목록 조회 관련
    List<Member> getMemberList(@Param("memberId") String memberId, @Param("memberName") String memberName);

    // 회원 통계 관련
    List<Map<String, Object>> getMemberStatsByYear();

    List<Map<String, Object>> getMemberStatsByMonth();

    List<Map<String, Object>> getMemberStatsByDay();

    // 회원 상태 수정
    int updateMemberStatus(@Param("memberId") String memberId, @Param("memberStatus") String memberStatus);

    // 미션 현황 관련
    List<Map<String, Object>> getMissionCompletionStats();

    List<Map<String, Object>> getMissionParticipationStats();

    // Board 관련 메서드
    List<Board> getBoardList();

    void updateBoardStatus(Map<String, Object> paramMap);

    // 게시글 상세 조회
    Board getBoardDetail(int boardId);

    List<Map<String, Object>> getDailyEmoDiary();

    List<Map<String, Object>> getTotalEmoDiary();

    List<Map<String, Object>> getDailyEmoBoard();

    List<Map<String, Object>> getTotalEmoBoard();

    // 이벤트
    List<Events> getEvents();

    void updateEvent(Events event);

    void insertEvent(Events event);

    void deleteEvent(int eventId);

    List<Map<String, Object>> getPastEventStats(@Param("eventId") int eventId, @Param("startDate") String startDate,
            @Param("endDate") String endDate, @Param("periodUnit") String periodUnit);

    List<Map<String, Object>> getTodayEventStats(@Param("eventId") int eventId);

    List<Map<String, Object>> getTotalEventStats();

    List<Map<String, Object>> getSignUpStats(@Param("startDate") String startDate, @Param("endDate") String endDate,
            @Param("periodUnit") String periodUnit);

    List<Map<String, Object>> getDropOutStats(@Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("periodUnit") String periodUnit);

    int getVisitorCount();

    List<Map<String, Object>> getTodayEventsStats();
}
