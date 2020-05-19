package com.ventasenlinea.ventas.rest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventasenlinea.ventas.domain.Cliente;
import com.ventasenlinea.ventas.domain.ContactoCliente;
import com.ventasenlinea.ventas.domain.DireccionCliente;
import com.ventasenlinea.ventas.domain.Producto;
import com.ventasenlinea.ventas.dto.ClienteDao;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/ventas/cliente")
public class ClienteRestController {
	
	ClienteDao clienteDao;
	 static int idClienteActual=0;
	@Autowired
    public void setClienteDao(ClienteDao clienteDao) {
		this.clienteDao=clienteDao;
	}
	@GetMapping("/")
	public ResponseEntity <List<Cliente>> getClientes(){
		List<Cliente> clientes= clienteDao.getAll();
		return new ResponseEntity<List<Cliente>>(clientes, HttpStatus.OK);
	}
	@GetMapping("/setearCliente/{idCliente}")
	public ResponseEntity <List<Integer>> setIdClienteActual(@PathVariable final Integer idCliente){
		idClienteActual=idCliente;
		List<Integer> id= new ArrayList<Integer>();
		id.add(idClienteActual);
		System.out.println("setea "+ id.toString());
		return new ResponseEntity <List<Integer>>(id, HttpStatus.OK);
	}
	
	@GetMapping("/getCliente")
	public ResponseEntity <List<Integer>> getCliente(){
		List<Integer> id= new ArrayList<Integer>();
		id.add(idClienteActual);
		System.out.println("Getea"+ id.toString());
		return new ResponseEntity <List<Integer>>(id, HttpStatus.OK);
	}
	
	@GetMapping("/{correo}/{password}")
	public ResponseEntity <List<Cliente>> inicioSeccion(@PathVariable("correo") final String correo,@PathVariable final String password){
		Cliente cliente= clienteDao.findByEmailAndPassword(correo, password);
		List<Cliente> clientes = new  ArrayList<Cliente>();
		clientes.add(cliente);
		return new ResponseEntity<List<Cliente>>(clientes,HttpStatus.OK);
	}
	@PostMapping(value = "/", consumes=  MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Cliente> registrarCliente(@RequestBody final Cliente cliente){
		System.out.println("ENTRA"+cliente.toString());
	Cliente clienteCreado=new Cliente();
	try {
		clienteCreado=clienteDao.insertarCliente(cliente);
	}catch(SQLException e) {
		e.printStackTrace();
	}
	return new ResponseEntity<Cliente>(clienteCreado,HttpStatus.OK);
	}
	/*
	@PostMapping(value = "/{text}", consumes=  MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DireccionCliente> registarDireccionCliente(@RequestBody final DireccionCliente direccionCliente){
		System.out.println("ENTRA"+direccionCliente.toString()+" "+ direccionCliente.getCliente().getIdCliente());
		DireccionCliente direccionCreada=new DireccionCliente();
	try {
		direccionCreada=clienteDao.insertarDireccionCliente(direccionCliente);
	}catch(SQLException e) {
		e.printStackTrace();
	}
	return new ResponseEntity<DireccionCliente>(direccionCreada,HttpStatus.OK);
	}
	@PostMapping(value = "/{text}/{text2}", consumes=  MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ContactoCliente> registrarContactoCliente(@RequestBody final ContactoCliente contactoCliente){
		System.out.println("ENTRA"+contactoCliente.toString());
		ContactoCliente contactoCreado=new ContactoCliente();
	try {
		contactoCreado=clienteDao.insertarContactoCliente(contactoCliente);
	}catch(SQLException e) {
		e.printStackTrace();
	}
	return new ResponseEntity<ContactoCliente>(contactoCreado,HttpStatus.OK);
	}
		*/       
}
