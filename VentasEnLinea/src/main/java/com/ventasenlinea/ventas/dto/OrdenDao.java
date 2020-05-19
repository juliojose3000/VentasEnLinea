package com.ventasenlinea.ventas.dto;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.ventasenlinea.ventas.domain.Categoria;
import com.ventasenlinea.ventas.domain.Cliente;
import com.ventasenlinea.ventas.domain.DetalleOrden;
import com.ventasenlinea.ventas.domain.Orden;
import com.ventasenlinea.ventas.domain.Producto;

@Repository
public class OrdenDao {

	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;
	private SimpleJdbcCall simpleJdbcCall;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.simpleJdbcCall = new SimpleJdbcCall(dataSource);
	}

	public Orden insert(Orden orden) throws SQLException {
		Connection connection = dataSource.getConnection();

		String sqlInsert = "{call VentasEnLinea_OrdenInsert(?,?,?)}";

		CallableStatement statement = connection.prepareCall(sqlInsert);

		statement.registerOutParameter(1, Types.INTEGER);
		statement.setDate(2, orden.getFechaOrden());
		statement.setInt(3, orden.getIdCliente());

		statement.execute();

		statement.close();
		connection.close();
		return orden;
	}

	public Cliente getClienteById(int idCliente) {
		String sqlSelect = "select id_cliente, nombre, apellidos, correo, [password] from Cliente where id_cliente = '"
				+ idCliente + "'";
		return jdbcTemplate.query(sqlSelect, new ClienteExtractor());

	}
	
	// este solo un carrito
	public Carrito getCarritoCliente(int idCliente) {
		String sqlSelect = "select consecutivo, id_cliente, c.id_producto, nombre, cantidad, precio \r\n"
				+ "from CarritoCompras c, Producto p where c.id_producto = p.id_producto and c.id_cliente=" + idCliente;
		return jdbcTemplate.query(sqlSelect, new CarritoExtractor());

	}
	
	//este me trae la lista de todos los productos del carrito
	public List<Carrito> getCarritosCliente(int idCliente) {
		String sqlSelect = "select consecutivo, id_cliente, c.id_producto, nombre, cantidad, precio \r\n"
				+ "from CarritoCompras c, Producto p where c.id_producto = p.id_producto and c.id_cliente="+idCliente;
		return jdbcTemplate.query(sqlSelect, new CarritoExtractor2());

	}

	public Cliente getCarritoByIdCliente(int idCliente) {
		String sqlSelect = "select* from CarritoCompras where id_cliente = '" + idCliente + "'";
		return jdbcTemplate.query(sqlSelect, new ClienteExtractor());

	}
	/*
	public List<Orden> getOrdenByIdCliente(int idCliente){
		String sqlSelect = "select numero_orden,fecha_orden,Cliente.id_cliente, Cliente.nombre, cliente.apellidos,cliente.correo, cliente.password "
				+ " from Orden,Cliente where  Cliente .id_cliente= Orden.id_cliente AND "
				+ " id_cliente = "+idCliente;
		return jdbcTemplate.query(sqlSelect, new OrdenExtractor());
	}
	*/
	public Producto getProductoById(int idProducto) {
		String sqlSelect = "Select  id_producto, nombre,existencias,precio,Producto.descripcion,"
				+ " minimo_existencias,Categoria.id_categoria,Categoria.descripcion_categoria"
				+ " from Producto,Categoria where id_producto = '" + idProducto+"'";

		return jdbcTemplate.query(sqlSelect, new ProductoExtractor());
	}


	public void insert(int idCliente) throws SQLException {
		
		Connection connection = dataSource.getConnection();

		Date date = new Date();
		java.sql.Date sDates = convertUtilToSql(date);

		String sqlInsert = "{call VentasEnLinea_OrdenInsert(?,?,?)}";

		CallableStatement statement = connection.prepareCall(sqlInsert);

		Cliente cliente = getClienteById(idCliente);
		List<Carrito> listaCarritosDeCliente = getCarritosCliente(idCliente);
		Orden orden = new Orden();
		orden.setCliente(cliente);
		orden.setFechaOrden(sDates);
		orden.setIdCliente(idCliente);
		orden.setCliente(cliente);
		
		statement.registerOutParameter(1, Types.INTEGER);
		statement.setDate(2, orden.getFechaOrden());
		statement.setInt(3, orden.getIdCliente());
		
		statement.execute();
		
		for (Carrito productoEnCarrito : listaCarritosDeCliente) {
			
			Producto producto = getProductoById(productoEnCarrito.getIdProducto());
			DetalleOrden detalleOrden = new DetalleOrden(producto, productoEnCarrito.getCantidad());
			
			orden.setDetalleOrden(detalleOrden);
			
			insertDetalleOrden(detalleOrden, statement.getInt(1));
			
		}//fin ciclo	
		
		statement.close();
		
		connection.close();

	}
	
	public void insertDetalleOrden(DetalleOrden detalleOrden, int numeroOrden) throws SQLException {
		
		Connection connection = dataSource.getConnection();
		String sqlInsert = "{call VentasEnLinea_DetalleOrdenInsert(?,?,?,?,?)}";
		CallableStatement statement = connection.prepareCall(sqlInsert);

	
		statement.setInt(1, numeroOrden);
		statement.setInt(2, detalleOrden.getProducto().getIdProducto());
		statement.setInt(3, detalleOrden.getCantidad());
		statement.setFloat(4, detalleOrden.getSubTotalOrden());
		statement.setFloat(5, detalleOrden.getPrecio());
		
		statement.execute();

		statement.close();
		connection.close();

	}
	

	private static java.sql.Date convertUtilToSql(java.util.Date uDate) {

		java.sql.Date sDate = new java.sql.Date(uDate.getTime());

		return sDate;

	}

	class ClienteRowMapper implements RowMapper<Cliente> {

		@Override
		public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
			Cliente cliente = new Cliente();
			cliente.setIdCliente(rs.getInt("id_cliente"));
			cliente.setNombre(rs.getString("nombre"));
			cliente.setApellidos(rs.getString("apellidos"));
			cliente.setCorreo(rs.getString("correo"));
			cliente.setPassword(rs.getString("password"));
			//cliente.setDireccion((DireccionCliente) (rs.getObject("direccion_cliente")));
			//cliente.setContactoCliente((ContactoCliente) (rs.getObject("contacto_cliente")));
			return cliente;
		}

	}
	/*
    private static final class OrdenExtractor implements ResultSetExtractor<Orden>{

		@Override
		public List<Orden> extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<Integer, Orden> map = new HashMap<Integer, Orden>();
			Orden orden =null;
			while (rs.next()) {
				if(orden==null) {
					orden=new Orden();
					orden.getCliente().set
				}
			}
		}
    	
    }
    */
	private static final class ClienteExtractor implements ResultSetExtractor<Cliente> {

		@Override
		public Cliente extractData(ResultSet rs) throws SQLException, DataAccessException {
			Cliente cliente = null;
			while (rs.next()) {

				if (cliente == null) {
					cliente = new Cliente();
					cliente.setIdCliente(rs.getInt("id_cliente"));
					cliente.setNombre(rs.getString("nombre"));
					cliente.setApellidos(rs.getString("apellidos"));
					cliente.setCorreo(rs.getString("correo"));
					cliente.setPassword(rs.getString("password"));
					//cliente.setDireccion((DireccionCliente) (rs.getObject("direccion_cliente")));
					//cliente.setContactoCliente((ContactoCliente) (rs.getObject("contacto_cliente")));

				} // if

			} // while
			return cliente;
		}

	}

	private static final class CarritoExtractor implements ResultSetExtractor<Carrito> {

		@Override
		public Carrito extractData(ResultSet rs) throws SQLException, DataAccessException {
			Carrito carrito = null;
			while (rs.next()) {
				if (carrito == null) {
					carrito = new Carrito();
					carrito.setConsecutivo(rs.getInt("consecutivo"));
					carrito.setIdCliente((rs.getInt("id_cliente")));
					carrito.setIdProducto((rs.getInt("id_producto")));
					carrito.setNombre(rs.getString("nombre"));
					carrito.setCantidad(rs.getInt("cantidad"));
					carrito.setPrecio(rs.getFloat("precio"));

				} // if

			} // while
			return carrito;
		}

	}
	
	private static final class CarritoExtractor2 implements ResultSetExtractor<List<Carrito>> {

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
	
	
	
	private static final class ProductoExtractor implements ResultSetExtractor<Producto> {

		@Override
		public Producto extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<Integer, Producto> map = new HashMap<Integer, Producto>();
			Producto producto = null;
			while (rs.next()) {
				Integer id = rs.getInt("id_producto");
				producto = map.get(id);
				if (producto == null) {
					producto = new Producto();
					producto.setIdProducto(id);
					producto.setNombre(rs.getString("nombre"));
					producto.setExistencias(rs.getInt("existencias"));
					producto.setPrecio(rs.getFloat("precio"));
					producto.setDescripcion(rs.getString("descripcion"));
					producto.setMinimoExistencias(rs.getInt("minimo_existencias"));
					map.put(id, producto);
				} // if
				int categoriaId = rs.getInt("id_categoria");
				if (categoriaId > 0) {
					Categoria categoria = new Categoria();
					categoria.setIdCategoria(categoriaId);
					categoria.setDescripcion(rs.getString("descripcion_categoria"));

					producto.setCategoria(categoria);

				} // if
			} // while
			return producto;
		}

	}

}
