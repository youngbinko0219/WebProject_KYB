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

		// 입력 값 검증
		boolean isValid = true;
		String errorMessage = "";

		if (fullName == null || fullName.isEmpty()) {
			isValid = false;
			errorMessage = "이름을 입력하세요.";
		} else if (email == null || email.isEmpty()) {
			isValid = false;
			errorMessage = "이메일을 입력하세요.";
		} else if (phoneNumber == null || phoneNumber.isEmpty()) {
			isValid = false;
			errorMessage = "전화번호를 입력하세요.";
		} else {
			// 이메일 형식 검증
			String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
			if (!email.matches(emailRegex)) {
				isValid = false;
				errorMessage = "올바른 이메일 형식을 입력하세요.";
			}

			// 전화번호 형식 검증
			String phoneRegex = "^\\d{10,11}$";
			if (!phoneNumber.matches(phoneRegex)) {
				isValid = false;
				errorMessage = "올바른 전화번호 형식을 입력하세요. (숫자 10~11자리)";
			}
		}

		if (!isValid) {
			// 에러 메시지를 세션에 저장하고 프로필 페이지로 리다이렉트
			session.setAttribute("errorMessage", errorMessage);
			response.sendRedirect(request.getContextPath() + "/user/profile");
			return;
		}

		// 사용자 정보 업데이트
		user.setFullName(fullName);
		user.setEmail(email);
		user.setPhoneNumber(phoneNumber);

		userDAO.updateUser(user); // 데이터베이스에 사용자 정보 업데이트
		session.setAttribute("user", user); // 업데이트된 정보를 세션에 저장

		// 수정 완료 메시지를 세션에 저장
		session.setAttribute("updateSuccess", true);

		response.sendRedirect(request.getContextPath() + "/dashboard");
	}
}
