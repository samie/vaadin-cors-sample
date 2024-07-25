package com.example.application;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Collection;

@WebFilter(filterName = "Vaadin CORS Filter", asyncSupported = true, urlPatterns = "/*")
public class CORSFilter extends HttpFilter {

    private String allowedOrigins = "https://samie.github.io";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String origin = request.getHeader("Origin");
        if (isOrginAllowed(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type");
            response.addHeader("Access-Control-Allow-Credentials", "true");
        }

        if ("options".equalsIgnoreCase(request.getMethod())) {
            response.addHeader("Access-Control-Allow-Methods", "GET, POST");
            response.addHeader("Access-Control-Allow-Headers", "content-type");
            response.getWriter().flush();
            return;
        }
        filterChain.doFilter(request, response);

        Collection<String> cookieHeaders = response.getHeaders("Set-Cookie");
        cookieHeaders.stream()
                .filter(c -> c.startsWith("JSESSIONID"))
                .findFirst()
                .ifPresent(sessionIdCookie -> response.setHeader("Set-Cookie", makeSameSite(sessionIdCookie)));
        cookieHeaders.stream()
              .filter(c -> !c.startsWith("JSESSIONID"))
              .forEach(c -> response.addHeader("Set-Cookie", c));

    }

    private String makeSameSite(String string) {
        return string.contains("SameSite=") ? string : string + ";SameSite=None; Secure";
    }

    private boolean isOrginAllowed(String origin) {
        return origin != null && allowedOrigins.contains(origin);
    }

    @Configuration
    public static class SpringBootSupport {

        @Bean
        public Filter vaadinCORSFilter() {
            return new CORSFilter();
        }
    }

}
