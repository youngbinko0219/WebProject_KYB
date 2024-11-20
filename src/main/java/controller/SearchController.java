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

	private static final int ITEMS_PER_PAGE = 10;

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
			// 페이지 번호 가져오기
			int freeboardTitlePage = getPageNumber(request.getParameter("freeboardTitlePage"));
			int qaBoardTitlePage = getPageNumber(request.getParameter("qaBoardTitlePage"));
			int dataBoardTitlePage = getPageNumber(request.getParameter("dataBoardTitlePage"));

			int freeboardContentPage = getPageNumber(request.getParameter("freeboardContentPage"));
			int qaBoardContentPage = getPageNumber(request.getParameter("qaBoardContentPage"));
			int dataBoardContentPage = getPageNumber(request.getParameter("dataBoardContentPage"));

			// 제목으로 검색
			List<FreeBoardDTO> freeboardTitleResultsAll = freeboardDAO.searchPostsByTitle(query);
			List<QaBoardDTO> qaBoardTitleResultsAll = qaBoardDAO.searchPostsByTitle(query);
			List<DataBoardDTO> dataBoardTitleResultsAll = dataBoardDAO.searchPostsByTitle(query);

			// 내용으로 검색
			List<FreeBoardDTO> freeboardContentResultsAll = freeboardDAO.searchPostsByContent(query);
			List<QaBoardDTO> qaBoardContentResultsAll = qaBoardDAO.searchPostsByContent(query);
			List<DataBoardDTO> dataBoardContentResultsAll = dataBoardDAO.searchPostsByContent(query);

			// 총 페이지 수 계산 및 페이징 처리
			int freeboardTitleTotalPages = calculateTotalPages(freeboardTitleResultsAll.size(), ITEMS_PER_PAGE);
			List<FreeBoardDTO> freeboardTitleResults = paginateList(freeboardTitleResultsAll, freeboardTitlePage,
					ITEMS_PER_PAGE);

			int qaBoardTitleTotalPages = calculateTotalPages(qaBoardTitleResultsAll.size(), ITEMS_PER_PAGE);
			List<QaBoardDTO> qaBoardTitleResults = paginateList(qaBoardTitleResultsAll, qaBoardTitlePage,
					ITEMS_PER_PAGE);

			int dataBoardTitleTotalPages = calculateTotalPages(dataBoardTitleResultsAll.size(), ITEMS_PER_PAGE);
			List<DataBoardDTO> dataBoardTitleResults = paginateList(dataBoardTitleResultsAll, dataBoardTitlePage,
					ITEMS_PER_PAGE);

			int freeboardContentTotalPages = calculateTotalPages(freeboardContentResultsAll.size(), ITEMS_PER_PAGE);
			List<FreeBoardDTO> freeboardContentResults = paginateList(freeboardContentResultsAll, freeboardContentPage,
					ITEMS_PER_PAGE);

			int qaBoardContentTotalPages = calculateTotalPages(qaBoardContentResultsAll.size(), ITEMS_PER_PAGE);
			List<QaBoardDTO> qaBoardContentResults = paginateList(qaBoardContentResultsAll, qaBoardContentPage,
					ITEMS_PER_PAGE);

			int dataBoardContentTotalPages = calculateTotalPages(dataBoardContentResultsAll.size(), ITEMS_PER_PAGE);
			List<DataBoardDTO> dataBoardContentResults = paginateList(dataBoardContentResultsAll, dataBoardContentPage,
					ITEMS_PER_PAGE);

			// 결과를 request에 저장
			request.setAttribute("query", query);

			// 제목 검색 결과
			request.setAttribute("freeboardTitleResults", freeboardTitleResults);
			request.setAttribute("freeboardTitlePage", freeboardTitlePage);
			request.setAttribute("freeboardTitleTotalPages", freeboardTitleTotalPages);

			request.setAttribute("qaBoardTitleResults", qaBoardTitleResults);
			request.setAttribute("qaBoardTitlePage", qaBoardTitlePage);
			request.setAttribute("qaBoardTitleTotalPages", qaBoardTitleTotalPages);

			request.setAttribute("dataBoardTitleResults", dataBoardTitleResults);
			request.setAttribute("dataBoardTitlePage", dataBoardTitlePage);
			request.setAttribute("dataBoardTitleTotalPages", dataBoardTitleTotalPages);

			// 내용 검색 결과
			request.setAttribute("freeboardContentResults", freeboardContentResults);
			request.setAttribute("freeboardContentPage", freeboardContentPage);
			request.setAttribute("freeboardContentTotalPages", freeboardContentTotalPages);

			request.setAttribute("qaBoardContentResults", qaBoardContentResults);
			request.setAttribute("qaBoardContentPage", qaBoardContentPage);
			request.setAttribute("qaBoardContentTotalPages", qaBoardContentTotalPages);

			request.setAttribute("dataBoardContentResults", dataBoardContentResults);
			request.setAttribute("dataBoardContentPage", dataBoardContentPage);
			request.setAttribute("dataBoardContentTotalPages", dataBoardContentTotalPages);

			// 검색 결과 페이지로 포워드
			request.getRequestDispatcher("/views/search/results.jsp").forward(request, response);
		} else {
			// 검색어가 없을 경우 메인 페이지로 리다이렉트
			response.sendRedirect(request.getContextPath() + "/dashboard");
		}
	}

	// 페이지 번호 가져오는 헬퍼 메서드
	private int getPageNumber(String pageParam) {
		int page = 1;
		if (pageParam != null && !pageParam.isEmpty()) {
			try {
				page = Integer.parseInt(pageParam);
			} catch (NumberFormatException e) {
				page = 1;
			}
		}
		return page;
	}

	// 리스트를 페이징 처리하는 메서드
	private <T> List<T> paginateList(List<T> list, int page, int itemsPerPage) {
		int totalItems = list.size();
		int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

		// 페이지 번호가 범위를 벗어나면 마지막 페이지로 설정
		if (page > totalPages) {
			page = totalPages;
		}

		int fromIndex = (page - 1) * itemsPerPage;
		int toIndex = Math.min(fromIndex + itemsPerPage, totalItems);

		if (fromIndex > toIndex || fromIndex < 0) {
			return list.subList(0, 0); // 빈 리스트 반환
		}

		return list.subList(fromIndex, toIndex);
	}

	// 총 페이지 수 계산 메서드
	private int calculateTotalPages(int totalItems, int itemsPerPage) {
		return (int) Math.ceil((double) totalItems / itemsPerPage);
	}
}
