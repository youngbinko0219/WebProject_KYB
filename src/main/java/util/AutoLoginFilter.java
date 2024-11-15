package util;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UserDAO;
import model.UserDTO;

public class AutoLoginFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession(false);

		// 세션이 없거나 로그인되지 않은 상태라면 자동 로그인 시도
		if (session == null || session.getAttribute("user") == null) {
			Cookie[] cookies = httpRequest.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("autoLogin".equals(cookie.getName())) {
						String username = cookie.getValue();

						// 데이터베이스에서 사용자 정보 조회
						UserDAO userDao = new UserDAO();
						UserDTO user = userDao.getUserByUsername(username);

						if (user != null) { // 유효한 사용자인 경우
							session = httpRequest.getSession();
							session.setAttribute("user", user); // 사용자 정보를 세션에 저장
						}

						userDao.close(); // 리소스 해제
						break;
					}
				}
			}
		}

		// 다음 필터 또는 요청 처리로 이동
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
	}
}
