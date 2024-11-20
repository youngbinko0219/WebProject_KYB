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

@WebServlet("/search")
public class SearchController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private FreeBoardDAO freeboardDAO;
	private QaBoardDAO qaBoardDAO;
	private DataBoardDAO dataBoardDAO;

	@Override
	public void init() throws ServletException {
		freeboardDAO = new FreeBoardDAO();
		qaBoardDAO = new QaBoardDAO();
		dataBoardDAO = new DataBoardDAO();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 검색어 가져오기
		String query = request.getParameter("query");

		if (query != null && !query.trim().isEmpty()) {
			// 각 게시판에서 검색 수행
			List<FreeBoardDTO> freeboardResults = freeboardDAO.searchPosts(query);
			List<QaBoardDTO> qaBoardResults = qaBoardDAO.searchPosts(query);
			List<DataBoardDTO> dataBoardResults = dataBoardDAO.searchPosts(query);

			// 결과를 request에 저장
			request.setAttribute("freeboardResults", freeboardResults);
			request.setAttribute("qaBoardResults", qaBoardResults);
			request.setAttribute("dataBoardResults", dataBoardResults);
			request.setAttribute("query", query);

			// 검색 결과 페이지로 포워드
			request.getRequestDispatcher("/views/search/results.jsp").forward(request, response);
		} else {
			// 검색어가 없을 경우 메시지 표시 또는 메인 페이지로 리다이렉트
			response.sendRedirect(request.getContextPath() + "/dashboard");
		}
	}
}
