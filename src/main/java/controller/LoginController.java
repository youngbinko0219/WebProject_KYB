package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UserDAO;
import model.UserDTO;

@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 사용자 입력 가져오기
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		// 사용자 인증 로직
		UserDAO userDao = new UserDAO();
		UserDTO user = userDao.authenticateUser(username, password);

		if (user != null) { // 로그인 성공
			HttpSession session = request.getSession();
			session.setAttribute("user", user);

			// 대시보드 페이지로 리디렉션
			response.sendRedirect(request.getContextPath() + "/views/board/dashboard/dashboard.jsp");
		} else { // 로그인 실패
			request.setAttribute("errorMessage", "아이디 또는 비밀번호가 올바르지 않습니다.");
			request.getRequestDispatcher("/views/user/login.jsp").forward(request, response);
		}

		if ("on".equals(request.getParameter("rememberMe"))) {
			Cookie loginCookie = new Cookie("autoLogin", username);
			loginCookie.setMaxAge(60 * 60 * 24 * 30); // 쿠키 유효 기간 설정 (30일)
			loginCookie.setPath("/"); // 모든 경로에 쿠키 적용
			response.addCookie(loginCookie);
		}

		userDao.close(); // DB 연결 해제
	}
}
