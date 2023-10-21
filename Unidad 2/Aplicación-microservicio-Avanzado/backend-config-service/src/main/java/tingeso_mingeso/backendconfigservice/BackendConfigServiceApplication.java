package tingeso_mingeso.backendconfigservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class BackendConfigServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendConfigServiceApplication.class, args);
	}

}
