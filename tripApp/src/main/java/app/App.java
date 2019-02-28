package app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@SpringBootApplication(scanBasePackages={"com.example.something"})
public class App {
  public static void main(final String[] args) {
    SpringApplication.run(App.class, args);
  }
}
