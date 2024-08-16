package example.jwtstart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringSecurityJwtStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityJwtStartApplication.class, args);
    }

}
