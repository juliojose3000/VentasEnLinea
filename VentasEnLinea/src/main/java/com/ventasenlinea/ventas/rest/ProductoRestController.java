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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventasenlinea.ventas.domain.Carrito;
import com.ventasenlinea.ventas.domain.Encargado;
import com.ventasenlinea.ventas.domain.Producto;
import com.ventasenlinea.ventas.dto.CarritoDao;
import com.ventasenlinea.ventas.dto.ClienteLogueadoDao;
import com.ventasenlinea.ventas.dto.ProductoDao;


@CrossOrigin(origins ="http://localhost:4200",maxAge=3600)
@RestController
@RequestMapping("/api/ventas/producto")
public class ProductoRestController {
	
public static Producto producto;
	
	@Autowired
	ProductoDao productoDao;
	
	@Autowired
	CarritoDao carritoDao;
	
	@Autowired
	ClienteLogueadoDao clienteLogueadoDao;
	
	@GetMapping("/")
	public ResponseEntity<List<Producto>> listAllEncagardos() {
		List<Producto> productos = productoDao.getAllProductos();
		//System.out.println(productos.toString());
		return new ResponseEntity<List<Producto>>(productos, HttpStatus.OK);
	}
	@GetMapping("/{idProducto}")
	public ResponseEntity<List<Producto>> getProductoId(@PathVariable final Integer idProducto){
		producto=productoDao.findByID(idProducto);
		List<Producto> productos= new ArrayList<Producto>();
		productos.add(producto);
		return new ResponseEntity<List<Producto>>(productos, HttpStatus.OK);
	}
	@GetMapping("/{name}/{texto}")
	public ResponseEntity<List<Producto>> getProductoName(@PathVariable final String name,@PathVariable final String texto){
		List<Producto> productos=productoDao.getProductoPorNombre(name);
		return new ResponseEntity<List<Producto>>(productos, HttpStatus.OK);
	}
	@GetMapping("/{text}/{text2}/{text3}")
	public ResponseEntity<List<Producto>> getTopProductos(@PathVariable final String text,@PathVariable final String text2,
			@PathVariable final String text3){
		List<Producto> productos=productoDao.getTopProductos();
		return new ResponseEntity<List<Producto>>(productos, HttpStatus.OK);
		
	}
	@PostMapping(value="/", consumes=  MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<Producto> insertarProducto(@RequestBody Producto producto){
		Producto productoCreado=null;
		try {
			System.out.println(producto.toString());
			productoCreado=productoDao.insert(producto);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Producto>(productoCreado,HttpStatus.OK);
	}

	// agregar un producto en carrito
	  @PostMapping(value = "/agregarEnCarrito", consumes = MediaType.APPLICATION_JSON_VALUE)
	  public ResponseEntity<Producto> insertarProductoEnCarrito(@RequestBody Carrito carrito) {
	    Producto productoCreado = null;
	    int quantity = carrito.getCantidad();
	    int idProducto = carrito.getIdProducto();
	    int idClienteActualLogueado = clienteLogueadoDao.getClienteLogueado().getIdCliente();
	    try {
	      productoCreado = carritoDao.addProductAtCar(idProducto, idClienteActualLogueado, quantity);
	    } catch (SQLException e) {
	      e.printStackTrace();
	    }
	    return new ResponseEntity<Producto>(productoCreado, HttpStatus.OK);
	  }
	@DeleteMapping(value="/{idProducto}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Producto> deleteProducto(@PathVariable final Integer idProducto) throws SQLException {
		System.out.println("entra "+ idProducto);
		productoDao.deleteProducto(idProducto);
		System.out.println("borra");
		return new ResponseEntity<Producto>(HttpStatus.NO_CONTENT);
	}
	@PutMapping(value = "/{idProducto}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Producto> updateProducto(@PathVariable final Integer idProducto, @RequestBody Producto producto){
		System.out.println(idProducto+" "+producto.toString());
    Producto productoActual=productoDao.findByID(idProducto);
   productoActual.setNombre(producto.getNombre());
   productoActual.setPrecio(producto.getPrecio());
   productoActual.setDescripcion(producto.getDescripcion());
   productoActual.setMinimoExistencias(producto.getMinimoExistencias());
   productoDao.update(productoActual);
   return new ResponseEntity<Producto>(producto,HttpStatus.OK);
   
   
     
	}
	
	

}
