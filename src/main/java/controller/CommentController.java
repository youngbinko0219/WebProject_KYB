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

@WebServlet("/comment/*")
public class CommentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommentDAO commentDAO;

	@Override
	public void init() throws ServletException {
		commentDAO = new CommentDAO();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();

		switch (path != null ? path : "") {
		case "/add":
			addComment(request, response);
			break;
		case "/edit":
			editComment(request, response);
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
		case "/delete":
			deleteComment(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void addComment(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
			return;
		}

		try {
			int postId = Integer.parseInt(request.getParameter("postId"));
			String content = request.getParameter("content");

			CommentDTO comment = new CommentDTO();
			comment.setBoardId(postId);
			comment.setUserId(userId);
			comment.setContent(content);

			boolean success = commentDAO.addComment(comment);

			if (success) {
				response.sendRedirect(request.getContextPath() + "/freeboard/view?id=" + postId);
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add comment.");
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid post ID.");
		}
	}

	private void editComment(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
			return;
		}

		try {
			int commentId = Integer.parseInt(request.getParameter("id"));
			String content = request.getParameter("content");

			CommentDTO existingComment = commentDAO.getCommentById(commentId);

			if (existingComment == null || existingComment.getUserId() != userId) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to edit this comment.");
				return;
			}

			existingComment.setContent(content);

			boolean success = commentDAO.updateComment(existingComment);

			if (success) {
				response.sendRedirect(request.getContextPath() + "/freeboard/view?id=" + existingComment.getBoardId());
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update comment.");
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid comment ID.");
		}
	}

	private void deleteComment(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
			return;
		}

		try {
			int commentId = Integer.parseInt(request.getParameter("id"));

			CommentDTO existingComment = commentDAO.getCommentById(commentId);

			if (existingComment == null || existingComment.getUserId() != userId) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to delete this comment.");
				return;
			}

			boolean success = commentDAO.deleteComment(commentId);

			if (success) {
				response.sendRedirect(request.getContextPath() + "/freeboard/view?id=" + existingComment.getBoardId());
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete comment.");
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid comment ID.");
		}
	}
}
