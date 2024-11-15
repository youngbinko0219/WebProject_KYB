package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.UserDAO;

@WebServlet("/ResetPasswordController")
public class ResetPasswordController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 사용자 입력을 통해 전달된 새 비밀번호와 확인 비밀번호를 가져옵니다.
		String newPassword = request.getParameter("newPassword");
		String confirmPassword = request.getParameter("confirmPassword");
		String username = request.getParameter("username"); // hidden 필드나 세션으로 전달된 사용자 아이디를 가져옵니다.

		// 비밀번호 확인
		if (newPassword == null || confirmPassword == null || !newPassword.equals(confirmPassword)) {
			request.setAttribute("message", "비밀번호가 일치하지 않습니다. 다시 시도해 주세요.");
			request.getRequestDispatcher("views/user/reset_password.jsp").forward(request, response);
			return;
		}

		// 비밀번호 업데이트를 위한 DAO 객체 생성
		UserDAO userDao = new UserDAO();
		boolean isUpdated = userDao.updatePasswordByUsername(username, newPassword);

		if (isUpdated) {
			// 업데이트 성공 시 메시지를 설정하고 로그인 페이지로 이동
			request.setAttribute("message", "비밀번호가 성공적으로 변경되었습니다. 로그인 해 주세요.");
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
		} else {
			// 업데이트 실패 시 오류 메시지를 설정하고 비밀번호 재설정 페이지로 다시 이동
			System.out.println("비밀번호 변경 실패 - Username: " + username);
			request.setAttribute("message", "비밀번호 변경에 실패했습니다. 다시 시도해 주세요.");
			request.getRequestDispatcher("views/user/reset_password.jsp").forward(request, response);
		}

		// 리소스 해제
		userDao.close();
	}
}
