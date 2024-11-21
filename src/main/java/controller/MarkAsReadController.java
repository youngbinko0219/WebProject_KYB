package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.MessageDAO;

@WebServlet("/MarkAsReadController")
public class MarkAsReadController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MessageDAO messageDAO;

	@Override
	public void init() throws ServletException {
		messageDAO = new MessageDAO();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 메시지 ID 가져오기
		try {
			int messageId = Integer.parseInt(request.getParameter("messageId"));

			// 메시지를 읽음으로 표시
			boolean success = messageDAO.markAsRead(messageId);

			if (success) {
				response.setStatus(HttpServletResponse.SC_OK); // 성공 상태 반환
			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 실패 상태 반환
			}
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 잘못된 메시지 ID
		}
	}
}
