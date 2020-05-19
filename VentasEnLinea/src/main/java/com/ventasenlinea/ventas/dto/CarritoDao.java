package com.ventasenlinea.ventas.dto;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.ventasenlinea.ventas.domain.Carrito;
import com.ventasenlinea.ventas.domain.Orden;
import com.ventasenlinea.ventas.domain.Producto;
import com.ventasenlinea.ventas.dto.ProductoDao.ProductoRowMapper;


@Repository
public class CarritoDao {

	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;
	private SimpleJdbcCall simpleJdbcCall;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.simpleJdbcCall = new SimpleJdbcCall(dataSource);
	}

	public List<Carrito> getCarritoCliente(int idCliente) {
		String sqlSelect = "select consecutivo, id_cliente, c.id_producto, nombre, cantidad, precio \r\n"
				+ "from CarritoCompras c, Producto p where c.id_producto = p.id_producto and c.id_cliente=" + idCliente;
		return jdbcTemplate.query(sqlSelect, new CarritoExtractor());

	}

	public Producto addProductAtCar(int idProducto, int idCliente, int cantidadProductos) throws SQLException {
	    //int cantidadProductos = 1; //esta es la cantidad por defecto que dentra cada producto dentro del carrito
         System.out.println("ENTRA "+ idCliente);
	    Connection connection = dataSource.getConnection();
	    String sqlInsert = "{call VentasEnLinea_InsertProductoEnCarrito(?,?,?,?)}";
	    CallableStatement statement = connection.prepareCall(sqlInsert);
	    statement.registerOutParameter(1, Types.INTEGER);
	    statement.setInt(2, idCliente);
	    statement.setInt(3, idProducto);
	    statement.setInt(4, cantidadProductos);

	    statement.execute();
	    statement.close();
	    connection.close();

	    return new Producto();
	  }

	public void deleteCarritoOfClient(int idCliente) {
	    this.simpleJdbcCall.withProcedureName("VentasEnLinea_CarritoDelete");
	    Map<String, Object> inParamMap = new HashMap<String, Object>();
	    inParamMap.put("id_cliente", idCliente);
	    SqlParameterSource in = new MapSqlParameterSource(inParamMap);
	    simpleJdbcCall.execute(in);
	  }
	public List<Integer> total(int idCliente) {
	    String sqlSelect = "select c.id_cliente, sum(cantidad*(precio+((precio)*0.13))) as total\r\n"
	        + "from CarritoCompras c, Producto p\r\n" + "where c.id_producto=p.id_producto and c.id_cliente="
	        + idCliente + " group by c.id_cliente";
	    
	    return jdbcTemplate.query(sqlSelect, new TotalExtractor());

	  }


	public void deleteProductoDeCarritoOfClient(int idCliente, int idProducto) {
		System.out.print("entra borrar item del carrito");
	    this.simpleJdbcCall.withProcedureName("VentasEnLinea_ProductoDeCarritoDelete");
	    Map<String, Object> inParamMap = new HashMap<String, Object>();
	    inParamMap.put("id_cliente", idCliente);
	    inParamMap.put("id_producto", idProducto);
	    SqlParameterSource in = new MapSqlParameterSource(inParamMap);
	    simpleJdbcCall.execute(in);
	  }

	class ProductoRowMapper implements RowMapper<Producto> {

		@Override
		public Producto mapRow(ResultSet rs, int rowNum) throws SQLException {
			Producto producto = new Producto();
			producto.setIdProducto(rs.getInt("id_producto"));
			producto.setNombre(rs.getString("nombre"));
			producto.setExistencias(rs.getInt("existencias"));
			producto.setPrecio(rs.getFloat("precio"));
			producto.setDescripcion(rs.getString("descripcion"));
			producto.setMinimoExistencias(rs.getInt("minimo_existencias"));
			return producto;
		}

	}

	private static final class CarritoExtractor implements ResultSetExtractor<List<Carrito>> {

		@Override
		public List<Carrito> extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<Integer, Carrito> map = new HashMap<Integer, Carrito>();
			Carrito carrito = null;
			while (rs.next()) {
				Integer consecutivo = rs.getInt("consecutivo");
				carrito = map.get(consecutivo);
				if (carrito == null) {
					carrito = new Carrito();
					carrito.setConsecutivo(consecutivo);
					carrito.setIdCliente((rs.getInt("id_cliente")));
					carrito.setIdProducto((rs.getInt("id_producto")));
					carrito.setNombre(rs.getString("nombre"));
					carrito.setCantidad(rs.getInt("cantidad"));
					carrito.setPrecio(rs.getFloat("precio"));

					map.put(consecutivo, carrito);
				} // if

			} // while
			return new ArrayList<Carrito>(map.values());
		}

	}

	private static final class TotalExtractor implements ResultSetExtractor<ArrayList<Integer>> {

		@Override
		public ArrayList<Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {

			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			int total = 0;
			while (rs.next()) {
				Integer id_cliente = rs.getInt("id_cliente");
				total = rs.getInt("total");

				map.put(id_cliente, total);

			} // while
			return new ArrayList<Integer>(map.values());
		}

	}
}
