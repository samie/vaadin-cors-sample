package com.example.application;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@WebFilter(filterName = "Vaadin CORS Filter", asyncSupported = true, urlPatterns = "/*")
public class CORSFilter extends HttpFilter {


        static {
            System.out.println("LOAD");
        }

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
            System.out.println("REGISTER");
        }

        @Override
        public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
            String origin = request.getHeader("Origin");
            if (isOrginAllowed(origin)) {
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                response.setHeader("Access-Control-Allow-Headers", "Content-Type");
            }

            if ("options".equalsIgnoreCase(request.getMethod())) {
                response.addHeader("Access-Control-Allow-Methods", "GET, POST");
                response.addHeader("Access-Control-Allow-Headers", "content-type");
                response.getWriter().flush();
                return;
            }
            filterChain.doFilter(request, response);

        }

        private boolean isOrginAllowed(String origin) {
            return true;
        }

    @Configuration
    public static class SpringBootSupport {

        @Bean
        public Filter vaadinCORSFilter() {
            return new CORSFilter();
        }
    }

}
