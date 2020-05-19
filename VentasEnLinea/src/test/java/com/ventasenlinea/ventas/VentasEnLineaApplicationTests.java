package com.ventasenlinea.ventas;

import java.sql.SQLException;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ventasenlinea.ventas.domain.Encargado;
import com.ventasenlinea.ventas.dto.EncargadoDao;
import com.ventasenlinea.ventas.dto.ProductoDao;
import com.ventasenlinea.ventas.repository.EncargadoRepository;
import com.ventasenlinea.ventas.rest.EncargadoRestController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VentasEnLineaApplicationTests {

	@Autowired
   EncargadoDao encargadoDao;
   @Autowired
   ProductoDao productoDao;
	@Autowired
	EncargadoRepository encargadoR;
	@Autowired
	EncargadoRestController encargadoRest;
	@Test
	public void contextLoads() {
	}
	@Test
	  @Transactional
	  public void pruebaEncargadoLog() throws SQLException {
		//Encargado encargado =encargadoDao.findByEmailAndPassword("ajosue19gmail.com", "abc");
		productoDao.deleteProducto(3);
	  System.out.println(productoDao.findByID(2).toString());        	
	}
	
	
}
