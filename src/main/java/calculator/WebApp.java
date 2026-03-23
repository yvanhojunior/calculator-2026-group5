package calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot entry point for the web-based calculator GUI.
 * Run this class to start the embedded web server (default port 8080).
 * The calculator UI is then available at http://localhost:8080
 */
@SpringBootApplication
public class WebApp {

    /**
     * Starts the embedded Tomcat web server and the Spring application context.
     *
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        SpringApplication.run(WebApp.class, args);
    }
}
