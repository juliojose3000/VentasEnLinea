package com.ventasenlinea.ventas.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventasenlinea.ventas.domain.Encargado;
import com.ventasenlinea.ventas.dto.EncargadoDao;
import com.ventasenlinea.ventas.repository.EncargadoRepository;


@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/ventas/encargado")
public class EncargadoRestController {

	
	EncargadoDao encargadoDao;
	
	@Autowired
	public void setEncargadoDao(EncargadoDao encargadoDao) {
		this.encargadoDao = encargadoDao;
	}

	@GetMapping("/{correo}/{password}")
	public ResponseEntity<Encargado> inicioSesion(@PathVariable("correo") final String correo,
			@PathVariable("password") final String password) {

		Encargado encargado = encargadoDao.findByEmailAndPassword(correo, password);
         System.out.println(encargado.toString());
		return new ResponseEntity<Encargado>(encargado, HttpStatus.OK);
	}
	






	@GetMapping("/")
	public ResponseEntity<List<Encargado>> listAllEncagardos() {
		List<Encargado> encargados = encargadoDao.getAll();
		return new ResponseEntity<List<Encargado>>(encargados, HttpStatus.OK);
	}

}
