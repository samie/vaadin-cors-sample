# Cross-site Vaadin Embedding Sample

This project demonstrates how to configure Cross-Origin Resource Sharing (CORS) in a Vaadin application with Spring Boot. 
It includes essential configuration and example files to help you quickly set up CORS in your own projects, ensuring 
secure resource sharing across different origins.

Demo is running at: https://samie.github.io/vaadin-cors-sample/


```mermaid
sequenceDiagram
    actor UserBrowser as User/Browser
    participant WebsiteDomain as Website (github.io)
    participant AppDomain as Application (fly.io)

    UserBrowser->>+WebsiteDomain: Open https://samie.github.io/vaadin-cors-sample/
    WebsiteDomain->>-UserBrowser: Serve HTML

    UserBrowser->>+AppDomain: Preflight OPTIONS Request
    AppDomain->>-UserBrowser: CORS Headers

    UserBrowser->>+AppDomain: Request newsletter-subscription.js
    AppDomain->>-UserBrowser: newsletter-subscription.js (Web Component)

    loop Vaadin application interaction
        UserBrowser-->>+AppDomain: User events (GET)
        AppDomain-->>-UserBrowser: UI Updates
     end

%%{
  init: {
    'theme':'base',
    'themeVariables': {
      'background': '#FFFFFF',
      'fontFamily': 'Arial',
      'fontSize': '20px',
      'lineColor': '#657892',
      'primaryColor': '#F1F5FB',
      'primaryBorderColor': '#BBC9DC',
      'primaryTextColor': '#0D1219',
      'secondaryColor': '#95C6FF',
      'secondaryBorderColor': '#BBC9DC',
      'secondaryTextColor': '#657892',
      'tertiaryColor': '#ff0000',
      'tertiaryBorderColor': '#BBC9DC',
      'tertiaryTextColor': '#0D1219',
      'signalColor':'#BBC9DC',
      'sequenceNumberColor':'#0D1219',
      'labelBoxBorderColor' :'#BBC9DC',
      'labelTextColor': '#0D1219',
      'actorBkg': '#F1F5FB',
      'actorBorder': '#BBC9DC',
      'actorTextColor': '#0D1219'
    }
  }
}%%

```

There are two branches in this repository:
1. [main](https://github.com/samie/vaadin-cors-sample/tree/main) - The embedded Vaadin application. It implements a simple newsletter subscription view (stores no data).
2. [website](https://github.com/samie/vaadin-cors-sample/tree/website) - Simple website that embeds the application running on another domain.

This project was created from [start.vaadin.com](https://start.vaadin.com/).

## Running the application

The project is a standard Maven project. To run it from the command line,
Run Maven build `mvn`, then open http://localhost:8080 in your browser.

You can also import the project to your IDE of choice as you would with any
Maven project. Read more on [how to import Vaadin projects to different IDEs](https://vaadin.com/docs/latest/guide/step-by-step/importing) (Eclipse, IntelliJ IDEA, NetBeans, and VS Code).

## Deploying to Production

To create a production build, call `mvn clean package -Pproduction`.
This will build a JAR file with all the dependencies and front-end resources,
ready to be deployed. The file can be found in the `target` folder after the build completes.

Once the JAR file is built, you can run it using
`java -jar target/my-app-1.0-SNAPSHOT.jar`

## Building the Docker Image

To build the Docker image, navigate to the project directory where the `Dockerfile` is located and run the following command:

```bash
docker build -t vaadin-cors-sample .
```

This command builds a Docker image using the `Dockerfile` in the current directory and tags the image as `vaadin-cors-sample`.

## Running the Docker Image

To run the Docker image, use the following command:

```bash
docker run -p 8080:8080 vaadin-cors-sample
```

This command runs the Docker image you previously built. The `-p 8080:8080` option maps the container's port 8080 to your machine's port 8080.

Now, you can access the application at http://localhost:8080.


## Required Vaadin application configuration

To make the application available for cross-origin requests, ensure the following:
1. Enable SSL for secure HTTPS connections.
2. Configure the session cookie header with SameSite=None and Secure.
3. Add necessary CORS headers `Access-Control-Allow-Origin`, `Access-Control-Allow-Credentials`, `Access-Control-Allow-Methods`, and `Access-Control-Allow-Headers` to responses. 
4. Properly handle the preflight `OPTIONS` requests. 
5. Use an explicit list of allowed domains for the `Access-Control-Allow-Origin` header instead of just `*`.

You can check the [source code](https://github.com/samie/vaadin-cors-sample/blob/main/src/main/java/com/example/application/Application.java#L40) to see how these were implemented for Spring Boot.

## Embedding to another website

This project demonstrates how to add required HTTP headers to serve the application across domains.
When embedding the application you need to do two things:
1. Register the web component in you html head section: `<script type="module" src="https://vaadin-cors-sample.fly.dev/web-component/newsletter-subscription.js"></script>`
2. Embed the web component to you page body: `<newsletter-subscription></newsletter-subscription>`

## Useful links

- Read the documentation at [vaadin.com/docs](https://vaadin.com/docs).
- Follow the tutorial at [vaadin.com/docs/latest/tutorial/overview](https://vaadin.com/docs/latest/tutorial/overview).
- Create new projects at [start.vaadin.com](https://start.vaadin.com/).
- Search UI components and their usage examples at [vaadin.com/docs/latest/components](https://vaadin.com/docs/latest/components).
- View use case applications that demonstrate Vaadin capabilities at [vaadin.com/examples-and-demos](https://vaadin.com/examples-and-demos).
- Build any UI without custom CSS by discovering Vaadin's set of [CSS utility classes](https://vaadin.com/docs/styling/lumo/utility-classes). 
- Find a collection of solutions to common use cases at [cookbook.vaadin.com](https://cookbook.vaadin.com/).
- Find add-ons at [vaadin.com/directory](https://vaadin.com/directory).
- Ask questions on [Stack Overflow](https://stackoverflow.com/questions/tagged/vaadin) or join our [Discord channel](https://discord.gg/MYFq5RTbBn).
- Report issues, create pull requests in [GitHub](https://github.com/vaadin).
