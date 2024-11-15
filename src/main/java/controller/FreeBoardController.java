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

		if (path == null || path.equals("/list")) {
			listPosts(request, response);
		} else if (path.equals("/view")) {
			viewPost(request, response);
		} else if (path.equals("/like")) {
			incrementLike(request, response);
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void listPosts(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int currentPage = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
		int pageSize = 10; // 페이지당 게시글 수
		int offset = (currentPage - 1) * pageSize;

		List<FreeBoardDTO> postList = freeBoardDAO.getFreeBoardList(offset, pageSize);
		int totalPosts = freeBoardDAO.getFreeBoardCount();
		int totalPages = (int) Math.ceil((double) totalPosts / pageSize);

		request.setAttribute("postList", postList);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("totalPages", totalPages);

		request.getRequestDispatcher("/views/board/free/list.jsp").forward(request, response);
	}

	private void viewPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int postId = Integer.parseInt(request.getParameter("id"));
			FreeBoardDTO post = freeBoardDAO.getPostById(postId);

			if (post != null) {
				request.setAttribute("post", post);
				request.getRequestDispatcher("/views/board/free/detail.jsp").forward(request, response);
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Post not found");
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid post ID");
		}
	}

	private void incrementLike(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			int postId = Integer.parseInt(request.getParameter("id"));
			boolean success = freeBoardDAO.incrementLikeCount(postId);

			if (success) {
				response.sendRedirect(request.getContextPath() + "/freeboard/view?id=" + postId); // 성공 시 해당 상세 페이지로
																									// 리디렉션
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to increment like count");
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid post ID");
		}
	}
}
