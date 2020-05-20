package start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;


@ComponentScan("basket")
@SpringBootApplication
public class StartRestServices {
    public static void main(String[] args) {
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:server-spring.xml");
        SpringApplication.run(StartRestServices.class, args);
    }
}

// URI for RESTED CLIENT in Chrome Browser: http://localhost:8080/basket/meciuri
