package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UserDAO;
import model.UserDTO;

@WebServlet("/user/profile")
public class ProfileController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserDAO userDAO;

	@Override
	public void init() throws ServletException {
		userDAO = new UserDAO(); // UserDAO 초기화
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO) session.getAttribute("user");

		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
			return;
		}

		// 세션의 사용자 ID로 DB에서 최신 프로필 정보 불러오기
		UserDTO userProfile = userDAO.getUserById(user.getUserId());
		request.setAttribute("user", userProfile);
		request.getRequestDispatcher("/views/user/profile.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO) session.getAttribute("user");

		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
			return;
		}

		// 사용자 입력값 가져오기
		String fullName = request.getParameter("fullName");
		String email = request.getParameter("email");
		String phoneNumber = request.getParameter("phoneNumber");

		if (fullName != null && !fullName.isEmpty() && email != null && !email.isEmpty() && phoneNumber != null
				&& !phoneNumber.isEmpty()) {
			user.setFullName(fullName);
			user.setEmail(email);
			user.setPhoneNumber(phoneNumber);

			userDAO.updateUser(user); // 데이터베이스에 사용자 정보 업데이트
			session.setAttribute("user", user); // 업데이트된 정보를 세션에 저장
		}

		response.sendRedirect(request.getContextPath() + "/views/board/dashboard/dashboard.jsp");
	}
}
