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
