package tingeso_mingeso.backendcuotasservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BackendCuotasServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendCuotasServiceApplication.class, args);
	}

}
