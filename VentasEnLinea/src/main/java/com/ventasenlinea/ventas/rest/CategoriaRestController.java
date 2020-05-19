package com.ventasenlinea.ventas.rest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
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
import org.springframework.web.client.RestOperationsExtensionsKt;

import com.ventasenlinea.ventas.domain.Categoria;
import com.ventasenlinea.ventas.domain.Producto;
import com.ventasenlinea.ventas.dto.CategoriaDao;
import com.ventasenlinea.ventas.dto.ProductoDao;

@CrossOrigin(origins ="http://localhost:4200",maxAge=3600)
@RestController
@RequestMapping("/api/ventas/categoria")
public class CategoriaRestController {
@Autowired
CategoriaDao categoriaDao;
@Autowired
ProductoDao productoDao;
@GetMapping("/")
public ResponseEntity<List<Categoria>>listAllCategorias(){
	List<Categoria> categorias=categoriaDao.getAllCategorias();
	return new ResponseEntity<List<Categoria>>(categorias,HttpStatus.OK);

}
@GetMapping("/{idCategoria}")
public ResponseEntity<Categoria> getCategoriaById(@PathVariable final Integer idCategoria){
	Categoria categoria= categoriaDao.FindByID(idCategoria);
	return new ResponseEntity<Categoria>(categoria,HttpStatus.OK);
}
@GetMapping("/{descripcion}/{texto}/{texto2}")
public ResponseEntity <List<Categoria>> getCategoriaByNombre(@PathVariable final String descripcion,@PathVariable final String texto,
		@PathVariable final String texto2){
	Categoria categoria= categoriaDao.findByDescripcion(descripcion);
	List <Categoria> categorias= new ArrayList<Categoria>();
	categorias.add(categoria);
	return new ResponseEntity<List<Categoria>>(categorias,HttpStatus.OK);
}
@GetMapping("/{categoria}/{texto}")
public ResponseEntity <List<Producto>> getProductosCategoria(@PathVariable final String categoria,@PathVariable final String texto){
	List<Producto> productos= productoDao.getProductosPorCategoria(categoria);
	return new ResponseEntity<List<Producto>>(productos,HttpStatus.OK);
	
}
@GetMapping("/{categoria}/{precio}/{texto}/{texto2}")
public ResponseEntity <List<Producto>> getProductosPrecio(@PathVariable final String categoria
		,@PathVariable final Integer precio,@PathVariable final String texto,@PathVariable final String texto2){
	List<Producto> productos= productoDao.getProductosPorPrecio(precio);
	return new ResponseEntity<List<Producto>>(productos,HttpStatus.OK);
	
}
@DeleteMapping(value="/{idCategoria}",consumes= MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<Categoria> deleteCategoria(@PathVariable final Integer idCategoria) throws SQLException{
	categoriaDao.deleteCategoria(idCategoria);
	return new ResponseEntity<Categoria>(HttpStatus.NO_CONTENT);
}
@PutMapping(value="/{idCategoria}",consumes= MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<Categoria> updateCategoria(@PathVariable final Integer idCategoria, @RequestBody Categoria categoria){
	System.out.println(idCategoria+categoria.toString());
	Categoria categoriaActual= categoriaDao.FindByID(idCategoria);
	categoriaActual.setDescripcion(categoria.getDescripcion());
	categoriaDao.update(categoriaActual);
	return new ResponseEntity<Categoria>(categoria,HttpStatus.OK);
}

@PostMapping(value="/", consumes=  MediaType.APPLICATION_JSON_VALUE )
public ResponseEntity<Categoria> insertarCategoria(@RequestBody Categoria categoria){
	Categoria categoriaCreada=null;
	try {
		categoriaCreada=categoriaDao.insert(categoria);
	}catch(SQLException e) {
		e.printStackTrace();
	}
	return new ResponseEntity<Categoria>(categoriaCreada,HttpStatus.OK);
}


}
