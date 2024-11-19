package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.FreeBoardDAO;
import model.FreeBoardDTO;
import util.CookieUtil;

@WebServlet("/freeboard/*")
public class FreeBoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private FreeBoardDAO freeBoardDAO;

	@Override
	public void init() throws ServletException {
		freeBoardDAO = new FreeBoardDAO();
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
		case "/like":
			toggleLike(request, response); // 좋아요 토글 메서드 추가
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
		case "/like":
			toggleLike(request, response); // 좋아요 토글 처리
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	// 글쓰기 페이지 로드
	private void showWritePage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
		} else {
			request.getRequestDispatcher("/views/board/free/write.jsp").forward(request, response);
		}
	}

	// 글쓰기 처리
	private void writePost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
			return;
		}

		// userId로 username 조회
		String username = freeBoardDAO.getUsernameByUserId(userId);
		if (username == null) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to retrieve username.");
			return;
		}

		// 파라미터로부터 제목과 내용 가져오기
		String title = request.getParameter("title");
		String content = request.getParameter("content");

		FreeBoardDTO post = new FreeBoardDTO();
		post.setUserId(userId);
		post.setUsername(username); // username 설정
		post.setTitle(title);
		post.setContent(content);
		post.setCreatedDate(new java.sql.Date(System.currentTimeMillis()));

		boolean success = freeBoardDAO.createPost(post);

		if (success) {
			response.sendRedirect(request.getContextPath() + "/freeboard/list?page=1");
		} else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create post.");
		}
	}

	// 게시글 목록 보기
	private void listPosts(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int currentPage = Integer
					.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
			int pageSize = 10;
			int offset = (currentPage - 1) * pageSize;

			List<FreeBoardDTO> postList = freeBoardDAO.getFreeBoardList(offset, pageSize);
			int totalPosts = freeBoardDAO.getFreeBoardCount();
			int totalPages = (int) Math.ceil((double) totalPosts / pageSize);

			request.setAttribute("postList", postList);
			request.setAttribute("currentPage", currentPage);
			request.setAttribute("totalPages", totalPages);

			request.getRequestDispatcher("/views/board/free/list.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading post list.");
		}
	}

	// 게시글 상세 보기
	private void viewPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int postId = Integer.parseInt(request.getParameter("id"));
			FreeBoardDTO post = freeBoardDAO.getPostById(postId);

			if (post != null) {
				request.setAttribute("post", post);

				// 좋아요 여부 확인 (로그인한 사용자와 쿠키 기반)
				String cookieName = "liked_posts";
				boolean liked = CookieUtil.containsValue(request, cookieName, String.valueOf(postId));
				request.setAttribute("liked", liked); // 좋아요 상태 전달

				request.getRequestDispatcher("/views/board/free/detail.jsp").forward(request, response);
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Post not found.");
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid post ID.");
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading post details.");
		}
	}

	private void toggleLike(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			int postId = Integer.parseInt(request.getParameter("id"));

			// 쿠키 이름
			String cookieName = "liked_posts";

			// 게시글 ID가 쿠키에 포함되어 있는지 확인
			boolean hasLiked = CookieUtil.containsValue(request, cookieName, String.valueOf(postId));

			if (hasLiked) {
				// 좋아요 취소
				freeBoardDAO.decrementLikeCount(postId); // 좋아요 수 감소
				CookieUtil.removeValue(request, response, cookieName, String.valueOf(postId)); // 쿠키에서 제거
			} else {
				// 좋아요 추가
				freeBoardDAO.incrementLikeCount(postId); // 좋아요 수 증가
				CookieUtil.addValue(request, response, cookieName, String.valueOf(postId)); // 쿠키에 추가
			}

			// 페이지 리다이렉트
			response.sendRedirect(request.getContextPath() + "/freeboard/view?id=" + postId);
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid post ID.");
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing like toggle.");
		}
	}

	// 게시글 삭제
	private void deletePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			Integer userId = (Integer) request.getSession().getAttribute("userId");
			if (userId == null) {
				response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
				return;
			}

			int postId = Integer.parseInt(request.getParameter("id"));
			FreeBoardDTO post = freeBoardDAO.getPostById(postId);

			if (post == null || !userId.equals(post.getUserId())) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to delete this post.");
				return;
			}

			boolean success = freeBoardDAO.deletePostById(postId);
			if (success) {
				response.sendRedirect(request.getContextPath() + "/freeboard/list?page=1");
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete post.");
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid post ID.");
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error deleting post.");
		}
	}

	// 게시글 수정 페이지 로드
	private void showEditPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int postId = Integer.parseInt(request.getParameter("id"));
		FreeBoardDTO post = freeBoardDAO.getPostById(postId);

		Integer userId = (Integer) request.getSession().getAttribute("userId");
		if (userId == null || post == null || !userId.equals(post.getUserId())) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to edit this post.");
			return;
		}

		request.setAttribute("post", post);
		request.getRequestDispatcher("/views/board/free/edit.jsp").forward(request, response);
	}

	// 게시글 수정 처리
	private void editPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer userId = (Integer) request.getSession().getAttribute("userId");
		int postId = Integer.parseInt(request.getParameter("id"));
		String title = request.getParameter("title");
		String content = request.getParameter("content");

		FreeBoardDTO post = freeBoardDAO.getPostById(postId);

		if (userId == null || post == null || !userId.equals(post.getUserId())) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to edit this post.");
			return;
		}

		post.setTitle(title);
		post.setContent(content);
		boolean success = freeBoardDAO.updatePost(post);

		if (success) {
			response.sendRedirect(request.getContextPath() + "/freeboard/view?id=" + postId);
		} else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update post.");
		}
	}
}
