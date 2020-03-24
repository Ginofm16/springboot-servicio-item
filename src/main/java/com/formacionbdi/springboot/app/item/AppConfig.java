package com.formacionbdi.springboot.app.item;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/*permite crear componentes de spring*/
@Configuration
public class AppConfig {

	@Bean("clienteRest")
	/*con esta anotacion de forma automativa va utilizar Ribbon balanceando la carga, ademas de permitir 
	 * colocar el nombre del servicio en la url y ya no el localhost:....*/
	@LoadBalanced
	/*El RestTemplate , es un cliente para trabajar con APIrest, cliente
	 * http para poder acceder a recursos qu estan en otrs microservicios*/
	public RestTemplate registrarRestTemplate() {
		return new RestTemplate();
	}
}
