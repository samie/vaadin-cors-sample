package com.example.application;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.util.Collection;

@WebFilter(filterName = "Vaadin CORS Filter", asyncSupported = true, urlPatterns = "/*")
public class VaadinCorsFilter extends HttpFilter {


    private String allowedOrigins;

    public VaadinCorsFilter(String allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public String getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(String allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain filterChain) throws IOException, ServletException {
        String origin = httpRequest.getHeader("Origin");
        if (isOriginAllowed("https://samie.github.io")) {
            httpResponse.setHeader("Access-Control-Allow-Origin", origin);
            httpResponse.addHeader("Access-Control-Allow-Credentials", "true");
        }

        if ("options".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.addHeader("Access-Control-Allow-Methods", "GET, POST");
            httpResponse.addHeader("Access-Control-Allow-Headers", "content-type");
            httpResponse.getWriter().flush();
            return;
        }
        filterChain.doFilter(httpRequest, httpResponse);
    }

    private boolean isOriginAllowed(String origin) {
        return origin != null && allowedOrigins.contains(origin);
    }

}
