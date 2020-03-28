package com.formacionbdi.springboot.app.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/*para habiliar hystrix, se encarga mediante un hilo separado de la comunicacion de los microservicios
 * envuelve ribbom y se encarga de manejar posibles errores*/
@EnableCircuitBreaker
@EnableEurekaClient
//aca de forma automatica feign se conecta con Ribbon. Luego se quita porque ya se ejecuta de forma automatica en Eureka
//@RibbonClient(name="servicio-productos")
//Feign es necesario porque se necesita habilitar feign como cliente para conectarnos a las API Rest
@EnableFeignClients
@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class SpringbootServicioItemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioItemApplication.class, args);
	}

}
