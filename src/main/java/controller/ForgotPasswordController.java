package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.UserDAO;
import model.UserDTO;

@WebServlet("/ForgotPasswordController")
public class ForgotPasswordController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 폼에서 제출된 아이디와 이메일 가져오기
		String username = request.getParameter("username");
		String email = request.getParameter("email");

		// UserDAO를 사용하여 아이디와 이메일이 등록된 회원인지 확인
		UserDAO userDao = new UserDAO();
		UserDTO user = userDao.getUserByUsernameAndEmail(username, email);

		if (user != null) {
			// 정보가 일치할 경우 비밀번호 변경 페이지로 포워드
			request.setAttribute("username", username);
			request.getRequestDispatcher("views/user/reset_password.jsp").forward(request, response);
		} else {
			// 정보가 일치하지 않을 경우 오류 메시지 설정
			request.setAttribute("message", "입력한 정보가 올바르지 않습니다.");
			request.getRequestDispatcher("views/user/forgot_password.jsp").forward(request, response);
		}

		// 리소스 해제
		userDao.close();
	}
}
