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
import model.FreeBoardDAO;
import model.FreeBoardDTO;
import model.MessageDAO;
import model.MessageDTO;
import model.UserDAO;
import model.UserDTO;

@WebServlet("/freeboard/*")
public class FreeBoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private FreeBoardDAO freeBoardDAO;
	private MessageDAO messageDAO;
	private UserDAO userDAO;

	@Override
	public void init() throws ServletException {
		freeBoardDAO = new FreeBoardDAO();
		messageDAO = new MessageDAO();
		userDAO = new UserDAO();
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

			// 게시글 상세 정보 조회
			FreeBoardDTO post = freeBoardDAO.getPostById(postId);

			// 댓글 리스트 조회 추가
			CommentDAO commentDAO = new CommentDAO();
			List<CommentDTO> comments = commentDAO.getFreeboardComments(postId);

			if (post != null) {
				request.setAttribute("post", post);
				request.setAttribute("comments", comments); // 댓글 전달
				request.getRequestDispatcher("/views/board/free/detail.jsp").forward(request, response);
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

	private void toggleLike(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			HttpSession session = request.getSession();
			Integer userId = (Integer) session.getAttribute("userId");

			if (userId == null) {
				response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
				return;
			}

			int postId = Integer.parseInt(request.getParameter("id"));

			// 사용자가 이미 좋아요를 눌렀는지 확인
			boolean hasLiked = freeBoardDAO.hasUserLikedPost(userId, postId);

			if (hasLiked) {
				// 좋아요 취소
				freeBoardDAO.decrementLikeCount(postId); // 좋아요 수 감소
				freeBoardDAO.removeUserLike(userId, postId); // 사용자 좋아요 기록 제거
			} else {
				// 좋아요 추가
				freeBoardDAO.incrementLikeCount(postId); // 좋아요 수 증가
				freeBoardDAO.addUserLike(userId, postId); // 사용자 좋아요 기록 추가

				// 좋아요 추가 후 알림 메시지 생성 (작성자가 아닌 경우에만 전송)
				FreeBoardDTO post = freeBoardDAO.getPostById(postId);
				if (post != null && !userId.equals(post.getUserId())) {
					UserDTO user = userDAO.getUserById(userId); // userId를 사용해 사용자 정보 가져오기
					if (user != null) {
						String username = user.getUsername(); // 사용자 이름 가져오기

						MessageDTO message = new MessageDTO();
						message.setReceiverId(post.getUserId()); // 게시글 작성자가 수신자
						message.setContent(username + "님이 게시글에 좋아요를 눌렀습니다."); // 사용자 이름을 메시지에 포함
						messageDAO.createNotificationMessage(message);
					}
				}
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
