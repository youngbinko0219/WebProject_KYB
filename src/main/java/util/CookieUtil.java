package util;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

	// 특정 쿠키를 가져오는 메서드
	public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
		if (request.getCookies() != null) {
			return Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(name)).findFirst();
		}
		return Optional.empty();
	}

	// 쿠키가 존재하지 않거나 만료되었는지 확인
	public static boolean isCookieExpired(HttpServletRequest request, String name) {
		Optional<Cookie> cookieOpt = getCookie(request, name);
		if (cookieOpt.isPresent()) {
			Cookie cookie = cookieOpt.get();
			long currentTime = System.currentTimeMillis();
			long cookieTime = Long.parseLong(cookie.getValue());
			// 24시간(밀리초 단위) 이내면 조회수 증가하지 않음
			return (currentTime - cookieTime) > 24 * 60 * 60 * 1000;
		}
		return true; // 쿠키가 없으면 만료된 것으로 간주
	}

	// 새로운 쿠키를 설정
	public static void setCookie(HttpServletResponse response, String name, long currentTimeMillis) {
		Cookie cookie = new Cookie(name, String.valueOf(currentTimeMillis));
		cookie.setMaxAge(24 * 60 * 60); // 24시간
		cookie.setPath("/"); // 루트 경로에서 유효
		response.addCookie(cookie);
	}

	// 쿠키에서 특정 값을 포함하는지 확인 (좋아요 여부 확인)
	public static boolean containsValue(HttpServletRequest request, String cookieName, String value) {
		return getCookie(request, cookieName).map(cookie -> Arrays.asList(cookie.getValue().split(",")).contains(value))
				.orElse(false);
	}

	// 쿠키에서 특정 값을 제거 (좋아요 취소)
	public static void removeValue(HttpServletRequest request, HttpServletResponse response, String cookieName,
			String value) {
		Optional<Cookie> cookieOpt = getCookie(request, cookieName);
		if (cookieOpt.isPresent()) {
			Cookie cookie = cookieOpt.get();
			String updatedValue = Arrays.stream(cookie.getValue().split(",")).filter(id -> !id.equals(value))
					.collect(Collectors.joining(","));
			setCookie(response, cookieName, updatedValue, 24 * 60 * 60);
		}
	}

	// 쿠키에 특정 값을 추가 (좋아요 추가)
	public static void addValue(HttpServletRequest request, HttpServletResponse response, String cookieName,
			String value) {
		Optional<Cookie> cookieOpt = getCookie(request, cookieName);
		String newValue;
		if (cookieOpt.isPresent()) {
			Cookie cookie = cookieOpt.get();
			newValue = cookie.getValue().isEmpty() ? value : cookie.getValue() + "," + value;
		} else {
			newValue = value;
		}
		setCookie(response, cookieName, newValue, 24 * 60 * 60);
	}

	// 쿠키 생성 및 업데이트
	public static void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);
		cookie.setPath("/"); // 모든 경로에서 유효
		response.addCookie(cookie);
	}
}
