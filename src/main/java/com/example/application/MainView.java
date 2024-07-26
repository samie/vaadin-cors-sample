package com.example.application;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.WebComponentExporter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.webcomponent.WebComponent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

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
        VerticalLayout layout = new VerticalLayout(emailField, subscribeButton, thankYouLabel);
        layout.setWidth("100%");
        layout.setPadding(true);
        emailField.setWidth("100%");
        add(layout);
    }

    /** This is for exporting the view as a web component. */
    public static class NewsletterSubscriptionExporter extends WebComponentExporter<MainView> {
        public NewsletterSubscriptionExporter() {
            super(TAG);
        }

        @Override
        protected void configureInstance(WebComponent<MainView> webComponent, MainView mainView) {
            // Nothing to configure
        }
    }
}
