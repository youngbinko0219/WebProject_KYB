package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LogoutController")
public class LogoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 세션 무효화 (로그아웃 처리)
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		// 자동 로그인 쿠키 삭제
		Cookie loginCookie = new Cookie("autoLogin", null);
		loginCookie.setMaxAge(0); // 쿠키 즉시 만료
		loginCookie.setPath("/"); // 애플리케이션 전역에서 적용
		response.addCookie(loginCookie);

		// 로그인 페이지로 리다이렉트
		response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
	}
}
