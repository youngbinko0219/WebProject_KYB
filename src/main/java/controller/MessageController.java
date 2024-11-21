package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.MessageDAO;
import model.MessageDTO;

@WebServlet("/MessageController")
public class MessageController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 로그인 확인
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			// 로그인하지 않은 경우 로그인 페이지로 리다이렉트
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
		} else {
			// 사용자의 받은 메시지 조회
			MessageDAO messageDAO = new MessageDAO();
			List<MessageDTO> messages = messageDAO.getMessagesForUser(userId);
			int unreadCount = messageDAO.getUnreadMessageCount(userId);

			// 메시지와 읽지 않은 메시지 개수를 request 속성으로 설정
			request.setAttribute("messages", messages);
			request.setAttribute("unreadCount", unreadCount);

			// `message.jsp`로 포워딩
			request.getRequestDispatcher("/views/common/message.jsp").forward(request, response);
		}
	}
}
