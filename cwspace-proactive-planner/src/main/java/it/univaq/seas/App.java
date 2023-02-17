package it.univaq.seas;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gianlucarea
 */
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        Planner.getInstance(true);
    }
}