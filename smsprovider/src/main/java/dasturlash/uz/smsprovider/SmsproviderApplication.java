package dasturlash.uz.smsprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmsproviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsproviderApplication.class, args);
	}

}
