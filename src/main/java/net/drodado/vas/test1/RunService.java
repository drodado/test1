package net.drodado.vas.test1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Mobile Communication Platform Service main class.
 * 
 * @author drodado
 *
 */
@SpringBootApplication
@ComponentScan(basePackages="net.drodado.vas.test1")
public class RunService {

	public static void main(String[] args) {
		SpringApplication.run(RunService.class, args);
	}

}
