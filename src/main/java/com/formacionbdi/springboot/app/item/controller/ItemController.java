package com.formacionbdi.springboot.app.item.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.models.Producto;
import com.formacionbdi.springboot.app.item.models.service.ItemService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/*anotacion que nos permitr actualizar los componentes, controladores, clases
 * anotadas con component o services, que se esta inyectando con value configuraciones y tambien el Environment;
 * se actualiza, se refresca el contexto, vuelve inyectar vuelve inicializar le componente con los cambios 
 * establecidos en tiempo real sin tener que reiniciar la aplicacion, mediante un endpoint de SpringActuator*/
@RefreshScope
@RestController
public class ItemController {

	private static Logger log = LoggerFactory.getLogger(ItemController.class);
	
	
	@Autowired
	private Environment env;
	
	@Autowired
	@Qualifier("serviceFeign")
	private ItemService itemService;
	
	/*inyeccion de dependencia mediante la anotacion value*/
	@Value("${configuracion.texto}")
	private String texto;
	
	@GetMapping("/listar")
	public List<Item> listar(){
		return itemService.findAll();
	}
	
	@HystrixCommand(fallbackMethod = "metodoAlternativo")
	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad) {
		return itemService.findById(id, cantidad);

	}
	
	public Item metodoAlternativo(Long id, Integer cantidad) {
		Item item = new Item();
		Producto producto = new Producto();
		
		item.setCantidad(cantidad);
		producto.setId(id);
		producto.setNombre("Samsung A50");
		producto.setPrecio(750.00);
		item.setProducto(producto);
		return item;
	}
	
	@GetMapping("/obtener-config")
	/*responseEntity es un objeto de Spring que representa el contenido que vamos a guardar en 
	 * la respuesta, en el cuerpo de la respuesta*/
	public ResponseEntity<?> obtenerConfig(@Value("${server.port}") String puerto){
		
		log.info(texto);
		
		Map<String, String> json = new HashMap<>();
		json.put("texto", texto);
		json.put("puerto", puerto);
		
		/*Aca de valida el ambiente en el que nos encontramos si es de desarrollo o produccion, para
		 hacer ello se necesita el Bean de Spring Environment; se pregunta por el profile, de que
		 ambiente corresponde(getActiveProfiles()), indice 0, primer elemento que seria el profile activo*/
		if(env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")) {
			//aca los valores se obtienen del repositorio Git, tambien usando el objeto Environment
			json.put("autor.nombre", env.getProperty("configuracion.autor.nombre"));
			json.put("autor.email", env.getProperty("configuracion.autor.correo"));
		}
		
		return new ResponseEntity<Map<String, String>>(json, HttpStatus.OK);
	}
}
