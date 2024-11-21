package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.CommentDAO;
import model.CommentDTO;
import model.FreeBoardDAO;
import model.FreeBoardDTO;
import model.MessageDAO;
import model.MessageDTO;
import model.QaBoardDAO;
import model.QaBoardDTO;
import model.UserDAO;
import model.UserDTO;

@WebServlet("/comment/*")
public class CommentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommentDAO commentDAO;
	private MessageDAO messageDAO;
	private FreeBoardDAO freeBoardDAO;
	private QaBoardDAO qaBoardDAO;

	@Override
	public void init() throws ServletException {
		commentDAO = new CommentDAO();
		messageDAO = new MessageDAO();
		freeBoardDAO = new FreeBoardDAO();
		qaBoardDAO = new QaBoardDAO();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();
		System.out.println("Path Info: " + path);

		switch (path != null ? path : "") {
		case "/addFreeboard":
			addFreeboardComment(request, response);
			break;
		case "/addQaboard":
			addQaboardComment(request, response);
			break;
		case "/updateFreeboard":
			updateFreeboardComment(request, response);
			break;
		case "/updateQaboard":
			updateQaboardComment(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();

		switch (path != null ? path : "") {
		case "/deleteFreeboard":
			deleteFreeboardComment(request, response);
			break;
		case "/deleteQaboard":
			deleteQaboardComment(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	// 자유게시판 댓글 추가
	private void addFreeboardComment(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
			return;
		}

		try {
			int boardId = Integer.parseInt(request.getParameter("boardId"));
			String content = request.getParameter("content");

			CommentDTO comment = new CommentDTO();
			comment.setBoardId(boardId);
			comment.setUserId(userId);
			comment.setContent(content);

			boolean success = commentDAO.addFreeboardComment(comment);

			if (success) {
				// UserDAO 객체를 사용해 현재 댓글 작성자의 username을 가져옴
				UserDAO userDAO = new UserDAO(); // UserDAO 객체 생성
				UserDTO user = userDAO.getUserById(userId); // userId를 사용해 사용자 정보 가져오기
				String username = (user != null) ? user.getUsername() : "알 수 없는 사용자";

				// 게시글 작성자 ID 가져오기
				FreeBoardDTO post = freeBoardDAO.getPostById(boardId);
				int postUserId = (post != null) ? post.getUserId() : -1;

				// 댓글 작성 후 알림 메시지 생성 (작성자가 아닌 경우에만 전송)
				if (postUserId != -1 && postUserId != userId) { // 자신이 작성한 글에 댓글을 달았을 때는 알림을 보내지 않음
					MessageDTO message = new MessageDTO();
					message.setReceiverId(postUserId); // 게시글 작성자가 수신자
					message.setContent(username + "님이 게시글에 댓글을 달았습니다: " + content);
					messageDAO.createNotificationMessage(message);
				}

				response.sendRedirect(request.getContextPath() + "/freeboard/view?id=" + boardId);
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add comment.");
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid board ID.");
		}
	}

	// 질문게시판 댓글 추가
	private void addQaboardComment(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
			return;
		}

		try {
			int boardId = Integer.parseInt(request.getParameter("boardId"));
			String content = request.getParameter("content");

			CommentDTO comment = new CommentDTO();
			comment.setBoardId(boardId);
			comment.setUserId(userId);
			comment.setContent(content);

			boolean success = commentDAO.addQaboardComment(comment);

			if (success) {
				// UserDAO 객체를 사용해 현재 댓글 작성자의 username을 가져옴
				UserDAO userDAO = new UserDAO(); // UserDAO 객체 생성
				UserDTO user = userDAO.getUserById(userId); // userId를 사용해 사용자 정보 가져오기
				String username = (user != null) ? user.getUsername() : "알 수 없는 사용자";

				// 게시글 작성자 ID 가져오기
				QaBoardDTO post = qaBoardDAO.getPostById(boardId);
				int postUserId = (post != null) ? post.getUserId() : -1;

				// 댓글 작성 후 알림 메시지 생성 (작성자가 아닌 경우에만 전송)
				if (postUserId != -1 && postUserId != userId) { // 자신이 작성한 글에 댓글을 달았을 때는 알림을 보내지 않음
					MessageDTO message = new MessageDTO();
					message.setReceiverId(postUserId); // 게시글 작성자가 수신자
					message.setContent(username + "님이 질문글에 댓글을 달았습니다: " + content);
					messageDAO.createNotificationMessage(message);
				}

				response.sendRedirect(request.getContextPath() + "/qaboard/view?id=" + boardId);
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add comment.");
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid board ID.");
		}
	}

	// 자유게시판 댓글 수정
	private void updateFreeboardComment(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
			return;
		}

		try {
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			String content = request.getParameter("content");

			CommentDTO comment = commentDAO.getFreeboardCommentById(commentId);
			if (comment == null || !Integer.valueOf(comment.getUserId()).equals(userId)) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to edit this comment.");
				return;
			}

			comment.setContent(content);

			boolean success = commentDAO.updateFreeboardComment(comment);

			if (success) {
				response.sendRedirect(request.getContextPath() + "/freeboard/view?id=" + comment.getBoardId());
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update comment.");
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid comment ID.");
		}
	}

	// 질문게시판 댓글 수정
	private void updateQaboardComment(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
			return;
		}

		try {
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			String content = request.getParameter("content");

			CommentDTO comment = commentDAO.getQaboardCommentById(commentId);
			if (comment == null || !Integer.valueOf(comment.getUserId()).equals(userId)) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to edit this comment.");
				return;
			}

			comment.setContent(content);

			boolean success = commentDAO.updateQaboardComment(comment);

			if (success) {
				response.sendRedirect(request.getContextPath() + "/qaboard/view?id=" + comment.getBoardId());
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update comment.");
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid comment ID.");
		}
	}

	// 자유게시판 댓글 삭제
	private void deleteFreeboardComment(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
			return;
		}

		try {
			int commentId = Integer.parseInt(request.getParameter("commentId"));

			CommentDTO comment = commentDAO.getFreeboardCommentById(commentId);
			if (comment == null || !Integer.valueOf(comment.getUserId()).equals(userId)) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to delete this comment.");
				return;
			}

			boolean success = commentDAO.deleteFreeboardComment(commentId);

			if (success) {
				response.sendRedirect(request.getContextPath() + "/freeboard/view?id=" + comment.getBoardId());
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete comment.");
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid comment ID.");
		}
	}

	// 질문게시판 댓글 삭제
	private void deleteQaboardComment(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
			return;
		}

		try {
			int commentId = Integer.parseInt(request.getParameter("commentId"));

			CommentDTO comment = commentDAO.getQaboardCommentById(commentId);
			if (comment == null || !Integer.valueOf(comment.getUserId()).equals(userId)) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to delete this comment.");
				return;
			}

			boolean success = commentDAO.deleteQaboardComment(commentId);

			if (success) {
				response.sendRedirect(request.getContextPath() + "/qaboard/view?id=" + comment.getBoardId());
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete comment.");
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid comment ID.");
		}
	}
}
