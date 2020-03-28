package com.formacionbdi.springboot.app.item.models.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.commons.models.entity.Producto;

/*@Service, es del tipo semantica para indicar que esta clase es del tipo
 * Business Service, es una fachada para acceder a los datos*/
@Service("serviceRestTemplate")
public class ItemServiceImpl implements ItemService {

	//inyeccion de AppConfig
	@Autowired
	private RestTemplate clienteRest;
	
	@Override
	public List<Item> findAll() {
		//gracias @LoadBalanced colocado en AppConfig, ya solo colocamos el nombre del servicio en la url,
		//url cliente que ya esta configurado en application.properties 
		List<Producto> productos = Arrays.asList(clienteRest.getForObject("http://servicio-productos/listar", Producto[].class));
		
		return productos.stream().map(p -> new Item(p, 1))
									.collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());
			
		Producto producto = clienteRest.getForObject("http://servicio-productos/ver/{id}", Producto.class,pathVariables);
		return new Item(producto, cantidad);
	}

	@Override
	public Producto save(Producto producto) {
		
		//(de argumento el objeto producto, que es lo que se quiere enviar)
		HttpEntity<Producto> body = new HttpEntity<>(producto);
		
		/*(endpoint, tipo de la peticion, enviar el reequest o body que contiene el objeto producto,
		   el tipo de dato en el que se quiere recibir el dato el JSON el APIrest que retorna)*/
		ResponseEntity<Producto> response = clienteRest.exchange("http://servicio-productos/crear", HttpMethod.POST, body, Producto.class);
		/*getBody(), retorna el cuerpo de la respuesta que sera de tipo Producto porque se le indico en
		 * los parametros de exchange*/
		Producto productoResponse = response.getBody();
		
		return productoResponse;
	}

	@Override
	public Producto update(Producto producto, Long id) {
		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());
		
		HttpEntity<Producto> body = new HttpEntity<>(producto);
		ResponseEntity<Producto> response = clienteRest.exchange("http://servicio-productos/editar/{id}", 
				HttpMethod.PUT, body, Producto.class, pathVariables);
		
		return response.getBody();
	}

	@Override
	public void delete(Long id) {
		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());
		
		clienteRest.delete("http://servicio-productos/eliminar/{id}",pathVariables);
		
	}

}
