package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.FreeBoardDAO;
import model.FreeBoardDTO;

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
			incrementLike(request, response);
			break;
		case "/write":
			showWritePage(request, response); // 글쓰기 페이지 로드
			break;
		case "/delete":
			deletePost(request, response);
			break;
		case "/edit":
			showEditPage(request, response);
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
			editPost(request, response); // 수정 처리
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	private void showWritePage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 세션에서 userId 가져오기
		Integer userId = (Integer) request.getSession().getAttribute("userId");

		if (userId == null) {
			// 로그인하지 않은 경우 로그인 페이지로 리디렉션
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
		} else {
			// 로그인된 경우 글쓰기 페이지로 포워드
			request.getRequestDispatcher("/views/board/free/write.jsp").forward(request, response);
		}
	}

	private void writePost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 세션에서 userId 가져오기
		Integer userId = (Integer) request.getSession().getAttribute("userId");

		if (userId == null) {
			// 로그인하지 않은 경우 로그인 페이지로 리디렉션
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
			return;
		}

		// 파라미터로부터 제목과 내용 가져오기
		String title = request.getParameter("title");
		String content = request.getParameter("content");

		FreeBoardDTO post = new FreeBoardDTO();
		post.setUserId(userId);
		post.setTitle(title);
		post.setContent(content);
		post.setCreatedDate(new java.sql.Date(System.currentTimeMillis()));

		boolean success = freeBoardDAO.createPost(post);

		if (success) {
			response.sendRedirect(request.getContextPath() + "/freeboard/list?page=1");
		} else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create post");
		}
	}

	private void listPosts(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int currentPage = Integer
					.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
			int pageSize = 10;
			int offset = (currentPage - 1) * pageSize;

			// 데이터 가져오기
			List<FreeBoardDTO> postList = freeBoardDAO.getFreeBoardList(offset, pageSize);
			int totalPosts = freeBoardDAO.getFreeBoardCount();
			int totalPages = (int) Math.ceil((double) totalPosts / pageSize);

			// 디버깅 로그
			System.out.println("Post List: " + postList); // 리스트 출력
			System.out.println("Total Posts: " + totalPosts);
			System.out.println("Total Pages: " + totalPages);

			// 데이터를 JSP로 전달
			request.setAttribute("postList", postList);
			request.setAttribute("currentPage", currentPage);
			request.setAttribute("totalPages", totalPages);

			// 리스트 페이지로 포워딩
			request.getRequestDispatcher("/views/board/free/list.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading post list");
		}
	}

	private void viewPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int postId = Integer.parseInt(request.getParameter("id"));

			// 조회수 증가
			freeBoardDAO.incrementViewCount(postId);

			// 게시글 상세 정보 조회
			FreeBoardDTO post = freeBoardDAO.getPostById(postId);

			if (post != null) {
				request.setAttribute("post", post);
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

	private void incrementLike(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			int postId = Integer.parseInt(request.getParameter("id"));
			boolean success = freeBoardDAO.incrementLikeCount(postId);

			if (success) {
				response.sendRedirect(request.getContextPath() + "/freeboard/view?id=" + postId); // 상세 페이지로 리디렉션
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to increment like count");
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid post ID");
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing like increment");
		}
	}

	private void deletePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			// 로그인된 사용자 ID 가져오기
			Integer loggedInUserId = (Integer) request.getSession().getAttribute("userId");
			if (loggedInUserId == null) {
				// 로그인하지 않은 경우
				response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
				return;
			}

			// 삭제할 게시글의 ID
			int postId = Integer.parseInt(request.getParameter("id"));

			// 게시글 정보 가져오기
			FreeBoardDTO post = freeBoardDAO.getPostById(postId);
			if (post == null) {
				// 게시글이 존재하지 않는 경우
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Post not found");
				return;
			}

			// 작성자인지 확인
			if (post.getUserId() != loggedInUserId) {
				// 작성자가 아닌 경우 접근 권한 없음
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to delete this post");
				return;
			}

			// 게시글 삭제 수행
			boolean success = freeBoardDAO.deletePostById(postId);
			if (success) {
				// 삭제 성공: 게시글 목록 페이지로 리디렉션
				response.sendRedirect(request.getContextPath() + "/freeboard/list?page=1");
			} else {
				// 삭제 실패: 에러 응답
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete post");
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid post ID");
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error deleting post");
		}
	}

	private void showEditPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 수정할 게시글의 ID 가져오기
		int postId = Integer.parseInt(request.getParameter("id"));

		// 게시글 데이터 가져오기
		FreeBoardDTO post = freeBoardDAO.getPostById(postId);

		// 사용자 인증 및 작성자 확인
		Integer userId = (Integer) request.getSession().getAttribute("userId");
		if (userId == null || post == null || userId != post.getUserId()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "수정 권한이 없습니다.");
			return;
		}

		// 게시글 데이터를 수정 페이지로 전달
		request.setAttribute("post", post);
		request.getRequestDispatcher("/views/board/free/edit.jsp").forward(request, response);
	}

	private void editPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 세션에서 사용자 ID 가져오기
		Integer userId = (Integer) request.getSession().getAttribute("userId");

		// 게시글 ID와 수정된 내용 가져오기
		int postId = Integer.parseInt(request.getParameter("id"));
		String title = request.getParameter("title");
		String content = request.getParameter("content");

		// 게시글 가져오기
		FreeBoardDTO post = freeBoardDAO.getPostById(postId);

		// 사용자 인증 및 작성자 확인
		if (userId == null || post == null || userId != post.getUserId()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "수정 권한이 없습니다.");
			return;
		}

		// 게시글 수정
		post.setTitle(title);
		post.setContent(content);
		boolean success = freeBoardDAO.updatePost(post);

		if (success) {
			response.sendRedirect(request.getContextPath() + "/freeboard/view?id=" + postId);
		} else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "수정에 실패했습니다.");
		}
	}
}
