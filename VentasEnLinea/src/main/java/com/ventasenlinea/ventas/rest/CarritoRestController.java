package com.ventasenlinea.ventas.rest;

import java.sql.SQLException;
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
import com.ventasenlinea.ventas.domain.Carrito;
import com.ventasenlinea.ventas.domain.Orden;
import com.ventasenlinea.ventas.domain.Producto;
import com.ventasenlinea.ventas.dto.CarritoDao;
import com.ventasenlinea.ventas.dto.OrdenDao;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/ventas/carrito")
public class CarritoRestController {

	CarritoDao carritoDao;
	OrdenDao ordenDao;

	@Autowired
	public void setEncargadoDao(CarritoDao carritoDao) {
		this.carritoDao = carritoDao;
	}
	
	@Autowired
	public void setOrdenDao(OrdenDao ordenDao) {
		this.ordenDao = ordenDao;
	}

	@GetMapping("/{idCliente}")
	public ResponseEntity<List<Carrito>> getCarritoCliente(@PathVariable final Integer idCliente) {
		List<Carrito> carrito = carritoDao.getCarritoCliente(idCliente);
		return new ResponseEntity<List<Carrito>>(carrito, HttpStatus.OK);
	}

	@GetMapping("/total/{idCliente}")
	public ResponseEntity<List<Integer>> total(@PathVariable final Integer idCliente) {
		List<Integer> total = carritoDao.total(idCliente);
		return new ResponseEntity<List<Integer>>(total, HttpStatus.OK);
	}

	@PostMapping(value = "/agregarOrden", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Orden> createProduct(@RequestBody final Orden orden) {
		Orden ordenCreada = null;
		try {
			System.out.println("aiudaaaa  " + orden.toString());
			ordenCreada = ordenDao.insert(orden);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Orden>(ordenCreada, HttpStatus.OK);

	}
	
	//el cliente elimino un producto de su carrito
	  @DeleteMapping(value = "/{idCliente}/{idProducto}", consumes = MediaType.APPLICATION_JSON_VALUE)
	  public ResponseEntity<Carrito> deleteProducto(@PathVariable final Integer idCliente, 
	                        @PathVariable final Integer idProducto) throws SQLException {
	    
	    carritoDao.deleteProductoDeCarritoOfClient(idCliente, idProducto);
	    
	    return new ResponseEntity<Carrito>(HttpStatus.NO_CONTENT);
	  
	  }
	  @DeleteMapping(value = "/{idCliente}", consumes = MediaType.APPLICATION_JSON_VALUE)
	  public ResponseEntity<Carrito> realizarLaCompra(@PathVariable final Integer idCliente) throws SQLException {
	    System.out.println("ID CLIENTE ORDEN "+ idCliente);
	    ordenDao.insert(idCliente);
	    
	    carritoDao.deleteCarritoOfClient(idCliente);
	    
	    return new ResponseEntity<Carrito>(HttpStatus.NO_CONTENT);
	  }
}


