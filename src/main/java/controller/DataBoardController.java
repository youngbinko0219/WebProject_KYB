package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.DataBoardDAO;
import model.DataBoardDTO;
import util.CookieUtil;

@WebServlet("/databoard/*")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
		maxFileSize = 1024 * 1024 * 10, // 10MB
		maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class DataBoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String UPLOAD_DIR = "/resources/upload";
	private DataBoardDAO dataBoardDAO;

	@Override
	public void init() throws ServletException {
		dataBoardDAO = new DataBoardDAO();
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
		case "/delete":
			deletePost(request, response);
			break;
		case "/edit":
			showEditPage(request, response);
			break;
		case "/write":
			showWritePage(request, response);
			break;
		case "/download":
			downloadFile(request, response);
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

	private void listPosts(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int currentPage = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
		int pageSize = 10;
		int offset = (currentPage - 1) * pageSize;

		List<DataBoardDTO> postList = dataBoardDAO.getDataBoardList(offset, pageSize);
		int totalPosts = postList.size();
		int totalPages = (int) Math.ceil((double) totalPosts / pageSize);

		request.setAttribute("postList", postList);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("totalPages", totalPages);
		request.getRequestDispatcher("/views/board/data/list.jsp").forward(request, response);
	}

	private void viewPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int postId = Integer.parseInt(request.getParameter("id"));

		// 쿠키 검사: "viewed_post_{postId}" 형식의 쿠키 사용
		String cookieName = "viewed_post_" + postId;
		if (CookieUtil.isCookieExpired(request, cookieName)) {
			// 쿠키가 없거나 만료되었으면 조회수 증가
			dataBoardDAO.incrementViewCount(postId);
			// 새로운 쿠키 설정
			CookieUtil.setCookie(response, cookieName, System.currentTimeMillis());
		}

		DataBoardDTO post = dataBoardDAO.getDataBoardById(postId);

		if (post != null) {
			request.setAttribute("post", post);
			request.getRequestDispatcher("/views/board/data/detail.jsp").forward(request, response);
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Post not found");
		}
	}

	private void writePost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
			return;
		}

		String title = request.getParameter("title");
		String content = request.getParameter("content");

		String uploadPath = getServletContext().getRealPath(UPLOAD_DIR);
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		List<String> originalFilenames = new ArrayList<>();
		List<String> storedFilenames = new ArrayList<>();

		// 파일 업로드 처리
		for (Part filePart : request.getParts()) {
			if (filePart.getName().equals("files") && filePart.getSize() > 0) {
				String originalFilename = filePart.getSubmittedFileName();
				String storedFilename = UUID.randomUUID().toString() + "_" + originalFilename;
				filePart.write(uploadPath + File.separator + storedFilename);
				originalFilenames.add(originalFilename);
				storedFilenames.add(storedFilename);
			}
		}

		// 게시글 정보 저장
		DataBoardDTO post = new DataBoardDTO();
		post.setUserId(userId);
		post.setTitle(title);
		post.setContent(content);
		post.setOriginalFilename(String.join(",", originalFilenames));
		post.setStoredFilename(String.join(",", storedFilenames));

		boolean success = dataBoardDAO.createDataBoardPost(post);

		if (success) {
			response.sendRedirect(request.getContextPath() + "/databoard/list?page=1");
		} else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create post");
		}
	}

	private void editPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
			return;
		}

		int postId = Integer.parseInt(request.getParameter("id"));
		String title = request.getParameter("title");
		String content = request.getParameter("content");

		// 기존 게시글 정보 가져오기
		DataBoardDTO post = dataBoardDAO.getDataBoardById(postId);

		if (post == null || !userId.equals(post.getUserId())) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to edit this post");
			return;
		}

		String uploadPath = getServletContext().getRealPath(UPLOAD_DIR);
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		// 기존 파일 삭제
		if (post.getStoredFilename() != null) {
			String[] existingStoredFiles = post.getStoredFilename().split(",");
			for (String storedFile : existingStoredFiles) {
				File oldFile = new File(uploadPath, storedFile.trim());
				if (oldFile.exists()) {
					oldFile.delete();
				}
			}
		}

		// 새로운 파일 정보 처리
		List<String> originalFilenames = new ArrayList<>();
		List<String> storedFilenames = new ArrayList<>();

		for (Part filePart : request.getParts()) {
			if (filePart.getName().equals("files") && filePart.getSize() > 0) {
				String originalFilename = filePart.getSubmittedFileName();
				String storedFilename = UUID.randomUUID().toString() + "_" + originalFilename;

				// 파일 저장
				filePart.write(uploadPath + File.separator + storedFilename);

				// 파일 정보 리스트에 추가
				originalFilenames.add(originalFilename);
				storedFilenames.add(storedFilename);
			}
		}

		// 게시글 정보 업데이트
		post.setTitle(title);
		post.setContent(content);
		post.setOriginalFilename(String.join(",", originalFilenames));
		post.setStoredFilename(String.join(",", storedFilenames));

		// 데이터베이스 업데이트
		boolean success = dataBoardDAO.updateDataBoardPost(post);

		if (success) {
			response.sendRedirect(request.getContextPath() + "/databoard/view?id=" + postId);
		} else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update post");
		}
	}

	private void deletePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
			return;
		}

		int postId = Integer.parseInt(request.getParameter("id"));
		DataBoardDTO post = dataBoardDAO.getDataBoardById(postId);

		if (post == null || !userId.equals(post.getUserId())) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to delete this post");
			return;
		}

		boolean success = dataBoardDAO.deleteDataBoardPost(postId);

		if (success) {
			response.sendRedirect(request.getContextPath() + "/databoard/list?page=1");
		} else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete post");
		}
	}

	private void showEditPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int postId = Integer.parseInt(request.getParameter("id"));
		DataBoardDTO post = dataBoardDAO.getDataBoardById(postId);

		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null || post == null || !userId.equals(post.getUserId())) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to edit this post");
			return;
		}

		request.setAttribute("post", post);
		request.getRequestDispatcher("/views/board/data/edit.jsp").forward(request, response);
	}

	private void showWritePage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
			return;
		}

		request.getRequestDispatcher("/views/board/data/write.jsp").forward(request, response);
	}

	public static String encodeFileName(HttpServletRequest request, String originalFileName) {
		try {
			String userAgent = request.getHeader("User-Agent");
			if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
				// IE 브라우저
				return java.net.URLEncoder.encode(originalFileName, "UTF-8").replaceAll("\\+", " ");
			} else if (userAgent.contains("Chrome")) {
				// Chrome 브라우저
				StringBuffer encodedFileName = new StringBuffer();
				for (char c : originalFileName.toCharArray()) {
					if (c > '~') {
						encodedFileName.append(java.net.URLEncoder.encode(String.valueOf(c), "UTF-8"));
					} else {
						encodedFileName.append(c);
					}
				}
				return encodedFileName.toString();
			} else {
				// 기타 브라우저
				return new String(originalFileName.getBytes("UTF-8"), "ISO-8859-1");
			}
		} catch (Exception e) {
			return originalFileName;
		}
	}

	private void downloadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 다운로드할 파일의 ID와 파일명을 파라미터로 가져옴
		String idParam = request.getParameter("id");
		String storedFileName = request.getParameter("storedFile");

		// ID 유효성 확인
		if (idParam == null || idParam.isEmpty() || storedFileName == null || storedFileName.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid File ID or file name");
			return;
		}

		int postId;
		try {
			postId = Integer.parseInt(idParam);
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid File ID");
			return;
		}

		// 게시글에서 파일 정보를 가져옴
		DataBoardDTO post = dataBoardDAO.getPostById(postId);
		if (post == null || post.getStoredFilename() == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Post or file not found");
			return;
		}

		// 서버의 업로드 디렉토리에서 파일 경로 확인
		String uploadPath = getServletContext().getRealPath("/resources/upload");
		File downloadFile = new File(uploadPath, storedFileName);

		// 파일 존재 여부 확인
		if (!downloadFile.exists()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found on server");
			return;
		}

		// 원본 파일 이름을 가져옴
		String originalFileName = getOriginalFileName(post, storedFileName);

		// 파일명 인코딩
		String encodedFileName = encodeFileName(request, originalFileName);

		// 응답 헤더 설정
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
		response.setContentLengthLong(downloadFile.length());

		// 파일 스트리밍 처리
		try (FileInputStream fis = new FileInputStream(downloadFile); OutputStream os = response.getOutputStream()) {
			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = fis.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during file transmission");
		}
	}

	private String getOriginalFileName(DataBoardDTO post, String storedFileName) {
		String[] storedFileNames = post.getStoredFilename().split(",");
		String[] originalFileNames = post.getOriginalFilename().split(",");

		for (int i = 0; i < storedFileNames.length; i++) {
			if (storedFileNames[i].trim().equals(storedFileName.trim())) {
				return originalFileNames[i].trim();
			}
		}
		return storedFileName; // 매칭되지 않을 경우 저장된 파일명을 반환
	}

}
