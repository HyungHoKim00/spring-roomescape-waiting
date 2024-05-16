package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.service.MemberService;
import roomescape.service.exception.InvalidTokenException;
import roomescape.service.exception.MemberNotFoundException;

import java.io.IOException;
import java.util.Arrays;

public class CheckLoginInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    public CheckLoginInterceptor(final MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response, final Object handler) throws IOException {
        final Cookie[] cookies = request.getCookies();
        final String token = extractTokenFromCookie(cookies);
        if (token == null) {
            response.sendRedirect("/login");
            return false;
        }
        try {
            memberService.validateToken(token);
        } catch (final InvalidTokenException | MemberNotFoundException e) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }

    private String extractTokenFromCookie(final Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .map(Cookie::getValue)
                .findAny()
                .orElse(null);
    }
}
