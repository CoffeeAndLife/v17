import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class V17SearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(V17SearchApplication.class, args);
	}

}
