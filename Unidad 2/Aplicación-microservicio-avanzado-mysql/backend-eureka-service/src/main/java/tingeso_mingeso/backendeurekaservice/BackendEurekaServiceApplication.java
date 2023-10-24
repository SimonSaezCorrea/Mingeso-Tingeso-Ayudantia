package tingeso_mingeso.backendeurekaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class BackendEurekaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendEurekaServiceApplication.class, args);
	}

}
