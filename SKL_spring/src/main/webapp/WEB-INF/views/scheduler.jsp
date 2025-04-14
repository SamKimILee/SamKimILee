<%@ page import="java.sql.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    String dbURL = "jdbc:oracle:thin:@localhost:1521/xepdb1"; // DB URL
    String dbUser = "hr1"; // DB 사용자
    String dbPassword = "hr1"; // DB 비밀번호
    //String sql = "UPDATE MEMBER SET USER_STATUS = 'inactive'" +
				//"WHERE LAST_LOGIN < ADD_MONTHS(SYSDATE, -12)" +
				//"AND USER_STATUS != 'inactive'"; // 쿼리
	String sql = (String) request.getAttribute("fileContent");
	//System.out.println("sql문 : "+sql);
%>

<%
    try (
        // DB 연결 및 PreparedStatement 준비
        Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
        PreparedStatement pstmt = conn.prepareStatement(sql)
    ) {
    	int rowsAffected = pstmt.executeUpdate();
    	//System.out.println("업데이트된 행의 수: " + rowsAffected);
    } catch (SQLException e) {
        e.printStackTrace(); // 예외 발생 시 출력
    }
%>