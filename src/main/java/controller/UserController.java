package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.UserDAO;
import model.UserDTO;

@WebServlet("/UserController")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 요청 인코딩 설정
		request.setCharacterEncoding("UTF-8");

		// 폼 데이터 수집
		String fullName = request.getParameter("fullName");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String phoneNumber = request.getParameter("phoneNumber");

		// UserDAO 객체 생성
		UserDAO userDao = new UserDAO();

		// 아이디 중복 확인
		if (userDao.isUsernameDuplicate(username)) {
			// 중복 아이디 경고 메시지 설정
			request.setAttribute("errorMessage", "중복 아이디 입니다. 다시 시도해 주세요.");
			userDao.close();
			request.getRequestDispatcher("views/user/signup.jsp").forward(request, response);
			return; // 중복이므로 가입 진행 중단
		}

		// 중복이 아닐 경우 회원가입 진행
		UserDTO user = new UserDTO(0, username, password, email, fullName, phoneNumber);
		boolean registrationSuccess = userDao.registerUser(user);

		userDao.close();

		if (registrationSuccess) {
			// 회원가입 성공 시 JavaScript를 통해 alert 표시 후 로그인 페이지로 이동
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write("<script>alert('회원가입이 성공적으로 완료되었습니다!'); location.href='"
					+ request.getContextPath() + "/views/user/login.jsp';</script>");
		} else {
			// 회원가입 실패 시 에러 메시지 설정하고 signup 페이지로 포워딩
			request.setAttribute("errorMessage", "회원가입에 실패했습니다. 다시 시도해 주세요.");
			request.getRequestDispatcher("views/user/signup.jsp").forward(request, response);
		}
	}
}