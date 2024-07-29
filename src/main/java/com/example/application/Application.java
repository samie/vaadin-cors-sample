package com.example.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import jakarta.servlet.Filter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public TomcatContextCustomizer sessionCookieConfigForCors() {
        return context -> {
            final Rfc6265CookieProcessor cookieProcessor = new Rfc6265CookieProcessor() {
                @Override
                public String generateHeader(Cookie cookie, HttpServletRequest request) {

                    // Needs to be secure
                    if (cookie.getName().startsWith("JSESSIONID")) {
                        cookie.setSecure(true);
                        cookie.setAttribute("SameSite", SameSiteCookies.NONE.getValue());
                        cookie.setAttribute("Partitioned", "true");
                    }
                    return super.generateHeader(cookie, request);
                }
            };
            context.setCookieProcessor(cookieProcessor);
        };
    }

    /** Publish CORS WebFilter in Spring Boot runtime. */
    @Configuration
    public static class VaadinCORSSupport {

        @Bean
        @Order
        public Filter vaadinCORSFilter(@Value("${embed.sites}") String allowedOrigins) {
            return new VaadinCorsFilter(allowedOrigins);
        }
    }
}
