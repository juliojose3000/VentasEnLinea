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

import com.ventasenlinea.ventas.domain.CategoriaMasVendida;
import com.ventasenlinea.ventas.domain.ClienteLogueado;
import com.ventasenlinea.ventas.domain.Orden;
import com.ventasenlinea.ventas.domain.Producto;
import com.ventasenlinea.ventas.domain.ReporteVenta;
import com.ventasenlinea.ventas.domain.Ventas;
import com.ventasenlinea.ventas.dto.ReporteVentaDao;

@CrossOrigin(origins ="http://localhost:4200",maxAge=3600)
@RestController
@RequestMapping("/api/ventas/reporte")
public class ReporteVentaRestController {
	
	@Autowired
	ReporteVentaDao reporteVenta;
	
@GetMapping("/")
public ResponseEntity <List<CategoriaMasVendida>> getTopCategorias(){
	List<CategoriaMasVendida> topCategorias= reporteVenta.getTopCategorias();
	return new ResponseEntity<List<CategoriaMasVendida>>(topCategorias,HttpStatus.OK);
}
@GetMapping("/totalVentas")
public ResponseEntity <List<Ventas>> getTotalVentas(){
	List<Ventas> topVentas= reporteVenta.getTopVentas();
	return new ResponseEntity<List<Ventas>>(topVentas,HttpStatus.OK);
}
	
@GetMapping("/{idCliente}")
public ResponseEntity<List<ReporteVenta>> getReporteByID(@PathVariable final Integer idCliente
		) throws SQLException{
	
	List<ReporteVenta> reporte= reporteVenta.getReporteVenta(idCliente);
	
	return new ResponseEntity<List<ReporteVenta>>(reporte, HttpStatus.OK);
}
@PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<ReporteVenta> insertarProductoEnCarrito(@RequestBody ClienteLogueado clienteLogueado) {
  ReporteVenta reporteCreado = null;

  try {
	  reporteCreado = reporteVenta.crearReporte(clienteLogueado);
  } catch (SQLException e) {
    e.printStackTrace();
  }
  return new ResponseEntity<ReporteVenta>(reporteCreado, HttpStatus.OK);
}

}
