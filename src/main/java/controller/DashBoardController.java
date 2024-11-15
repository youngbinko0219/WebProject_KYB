package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.DataBoardDAO;
import model.DataBoardDTO;
import model.FreeBoardDAO;
import model.FreeBoardDTO;
import model.QaBoardDAO;
import model.QaBoardDTO;

@WebServlet("/dashboard")
public class DashBoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private FreeBoardDAO freeBoardDAO;
	private DataBoardDAO dataBoardDAO;
	private QaBoardDAO qaBoardDAO;

	@Override
	public void init() throws ServletException {
		freeBoardDAO = new FreeBoardDAO();
		dataBoardDAO = new DataBoardDAO();
		qaBoardDAO = new QaBoardDAO();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 자유 게시판 최신 3개 글 가져오기
		List<FreeBoardDTO> latestFreePosts = freeBoardDAO.getLatestPosts(3);
		// 자료실 게시판 최신 3개 글 가져오기
		List<DataBoardDTO> latestDataPosts = dataBoardDAO.getLatestPosts(3);
		// 질문 게시판 최신 3개 글 가져오기
		List<QaBoardDTO> latestQAPosts = qaBoardDAO.getLatestPosts(3);

		// 요청 속성 설정
		request.setAttribute("latestFreePosts", latestFreePosts);
		request.setAttribute("latestDataPosts", latestDataPosts);
		request.setAttribute("latestQAPosts", latestQAPosts);

		// 대시보드 JSP로 포워딩
		request.getRequestDispatcher("/views/board/dashboard/dashboard.jsp").forward(request, response);
	}
}