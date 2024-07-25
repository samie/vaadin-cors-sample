package com.example.application.views.main;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.WebComponentExporter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.webcomponent.WebComponent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@PageTitle("Vaadin CORS sample")
@Route(value = "")
public class MainView extends Div {

    public static final String TAG = "newsletter-subscription";

    public MainView() {

//        HorizontalLayout layout = new HorizontalLayout(FlexComponent.Alignment.BASELINE);
        TextField emailField = new TextField("Email");
        Button subscribeButton = new Button("Subscribe");
        Text thankYouLabel = new Text("");

        subscribeButton.addClickListener(click -> {
            thankYouLabel.setText("Thank you for subscribing!");
            emailField.setVisible(false);
            subscribeButton.setVisible(false);
        });
        HorizontalLayout layout = new HorizontalLayout(FlexComponent.Alignment.BASELINE, emailField, subscribeButton, thankYouLabel);
        layout.setWidth("100%");
        layout.setPadding(true);
        emailField.setWidth("80%");
        subscribeButton.setWidth("20%");
        add(layout);
    }

    public static class NewsletterSubscriptionExporter extends WebComponentExporter<MainView> {
        public NewsletterSubscriptionExporter() {
            super(TAG);
        }

        @Override
        protected void configureInstance(WebComponent<MainView> webComponent, MainView mainView) {
            // Nothing to configure
        }
    }

    @WebFilter(filterName = "CORS Filter", asyncSupported = true, value = "/*")
    public static class CORSFilter implements Filter {
        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;

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
            filterChain.doFilter(servletRequest, servletResponse);

        }

        private boolean isOrginAllowed(String origin) {
            return true;
        }
    }
}
