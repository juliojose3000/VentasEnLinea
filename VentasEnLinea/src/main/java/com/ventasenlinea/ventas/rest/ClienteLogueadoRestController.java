package com.ventasenlinea.ventas.rest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventasenlinea.ventas.domain.Cliente;
import com.ventasenlinea.ventas.domain.ClienteLogueado;
import com.ventasenlinea.ventas.dto.ClienteLogueadoDao;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/ventas/clienteLogueado")
public class ClienteLogueadoRestController {

	@Autowired
	ClienteLogueadoDao clienteLogueadoDao;
	
	@GetMapping("/")
	public ResponseEntity <List<ClienteLogueado>> getClientes(){
		List<ClienteLogueado> clientes=new ArrayList<ClienteLogueado>();
		clientes.add( clienteLogueadoDao.getClienteLogueado());
	//	System.out.print(clientes.toString());
		return new ResponseEntity<List<ClienteLogueado>>(clientes, HttpStatus.OK);
	}
	
	@PostMapping(value = "/", consumes=  MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClienteLogueado> registrarClienteLogueado(@RequestBody final Cliente cliente){
		//System.out.println("ENTRA"+cliente.toString());
		ClienteLogueado clienteLogueao=  new ClienteLogueado();
		clienteLogueao.setIdCliente(cliente.getIdCliente());
		ClienteLogueado clienteCreado=new ClienteLogueado();
	try {
		clienteCreado=clienteLogueadoDao.insertarClienteLogueado(clienteLogueao);
	}catch(SQLException e) {
		e.printStackTrace();
	}
	return new ResponseEntity<ClienteLogueado>(clienteCreado,HttpStatus.OK);
	}
	
	@DeleteMapping(value="/", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClienteLogueado> deleteProducto() {
		System.out.println("Borrar cliente logeuado");
		clienteLogueadoDao.deleteClienteLogueado();
		
		return new ResponseEntity<ClienteLogueado>(HttpStatus.NO_CONTENT);
	}
	
	}

