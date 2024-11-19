package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.CommentDAO;
import model.CommentDTO;
import model.QaBoardDAO;
import model.QaBoardDTO;
import model.UserDAO;
import util.CookieUtil;

@WebServlet("/qaboard/*")
public class QaBoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private QaBoardDAO qaBoardDAO;

	@Override
	public void init() throws ServletException {
		qaBoardDAO = new QaBoardDAO();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();

		switch (path != null ? path : "/list") {
		case "/list":
			listPosts(request, response);
			break;
		case "/view":
			viewPost(request, response);
			break;
		case "/write":
			showWritePage(request, response);
			break;
		case "/delete":
			deletePost(request, response);
			break;
		case "/edit":
			showEditPage(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();

		switch (path) {
		case "/write":
			writePost(request, response);
			break;
		case "/edit":
			editPost(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	// 게시글 목록 페이지
	private void listPosts(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int currentPage = Integer
					.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
			int pageSize = 10;
			int offset = (currentPage - 1) * pageSize;

			List<QaBoardDTO> postList = qaBoardDAO.getQABoardList(offset, pageSize);
			int totalPosts = qaBoardDAO.getQABoardCount();
			int totalPages = (int) Math.ceil((double) totalPosts / pageSize);

			request.setAttribute("postList", postList);
			request.setAttribute("currentPage", currentPage);
			request.setAttribute("totalPages", totalPages);

			request.getRequestDispatcher("/views/board/qa/list.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading post list");
		}
	}

	// 게시글 작성 페이지
	private void showWritePage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
		} else {
			request.getRequestDispatcher("/views/board/qa/write.jsp").forward(request, response);
		}
	}

	// 게시글 작성
	private void writePost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
			return;
		}

		UserDAO userDAO = new UserDAO();
		String username = userDAO.getUsernameByUserId(userId); // userId를 기반으로 username 조회

		String title = request.getParameter("title");
		String content = request.getParameter("content");

		System.out.println("QaBoardDTO 객체 생성 시도");
		QaBoardDTO post = new QaBoardDTO();
		System.out.println("QaBoardDTO 객체 생성 완료");

		post.setUserId(userId);
		post.setUsername(username); // username 추가
		post.setTitle(title);
		post.setContent(content);

		boolean success = qaBoardDAO.createQAPost(post);

		if (success) {
			response.sendRedirect(request.getContextPath() + "/qaboard/list?page=1");
		} else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create post");
		}
	}

	// 게시글 상세 보기
	private void viewPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int postId = Integer.parseInt(request.getParameter("id"));

			// 쿠키 이름 설정 (예: "viewed_qa_post_{postId}")
			String cookieName = "viewed_qa_post_" + postId;

			// 쿠키를 통해 조회수 증가 여부 확인
			if (CookieUtil.isCookieExpired(request, cookieName)) {
				// 조회수 증가 및 쿠키 설정
				qaBoardDAO.incrementViewCount(postId);
				CookieUtil.setCookie(response, cookieName, System.currentTimeMillis());
			}

			// 게시글 상세 정보 조회
			QaBoardDTO post = qaBoardDAO.getPostById(postId);

			// 댓글 리스트 조회
			CommentDAO commentDAO = new CommentDAO();
			List<CommentDTO> comments = commentDAO.getQABoardComments(postId);

			if (post != null) {
				request.setAttribute("post", post);
				request.setAttribute("comments", comments); // 댓글 전달
				request.getRequestDispatcher("/views/board/qa/detail.jsp").forward(request, response);
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Post not found");
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid post ID");
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading post details");
		}
	}

	// 게시글 삭제
	private void deletePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			Integer loggedInUserId = (Integer) request.getSession().getAttribute("userId");
			if (loggedInUserId == null) {
				response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
				return;
			}

			int postId = Integer.parseInt(request.getParameter("id"));
			QaBoardDTO post = qaBoardDAO.getQAPostById(postId);

			if (post == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Post not found");
				return;
			}

			if (!loggedInUserId.equals(post.getUserId())) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to delete this post");
				return;
			}

			boolean success = qaBoardDAO.deleteQAPostById(postId);
			if (success) {
				response.sendRedirect(request.getContextPath() + "/qaboard/list?page=1");
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete post");
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid post ID");
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error deleting post");
		}
	}

	// 게시글 수정 페이지
	private void showEditPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int postId = Integer.parseInt(request.getParameter("id"));
		QaBoardDTO post = qaBoardDAO.getQAPostById(postId);

		Integer userId = (Integer) request.getSession().getAttribute("userId");
		if (userId == null || post == null || !userId.equals(post.getUserId())) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to edit this post.");
			return;
		}

		request.setAttribute("post", post);
		request.getRequestDispatcher("/views/board/qa/edit.jsp").forward(request, response);
	}

	// 게시글 수정
	private void editPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer userId = (Integer) request.getSession().getAttribute("userId");
		int postId = Integer.parseInt(request.getParameter("id"));
		String title = request.getParameter("title");
		String content = request.getParameter("content");

		QaBoardDTO post = qaBoardDAO.getQAPostById(postId);

		if (userId == null || post == null || !userId.equals(post.getUserId())) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to edit this post.");
			return;
		}

		post.setTitle(title);
		post.setContent(content);
		boolean success = qaBoardDAO.updateQAPost(post);

		if (success) {
			response.sendRedirect(request.getContextPath() + "/qaboard/view?id=" + postId);
		} else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update post.");
		}
	}
}
