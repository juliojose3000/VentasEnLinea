package com.ventasenlinea.ventas.dto;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

import com.ventasenlinea.ventas.domain.Categoria;
import com.ventasenlinea.ventas.domain.Encargado;
import com.ventasenlinea.ventas.domain.Producto;





@Repository
public class ProductoDao {
	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;
	private SimpleJdbcCall simpleJdbcCall;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.simpleJdbcCall=new SimpleJdbcCall(dataSource);
	}
	
	public List<Producto> getAllProductos() {
		String sqlSelect = "Select id_producto,nombre,existencias,"
				+ "precio,Producto.descripcion,minimo_existencias,Categoria.id_categoria,Categoria.descripcion_categoria "
				+ "from Producto,Categoria where Producto.id_categoria = Categoria.id_categoria";
			
			
	return jdbcTemplate.query(sqlSelect, new ProductoExtractor());

}
	public List<Producto> getProductoPorNombre(String name){
		String sqlSelect="Select  id_producto, nombre,existencias,precio,Producto.descripcion,"
				+ " minimo_existencias,Categoria.id_categoria,Categoria.descripcion_categoria"+
				" from Producto,Categoria where nombre like '%"+name.trim()+"%' AND Producto.id_categoria=Categoria.id_categoria ";
	
		return jdbcTemplate.query(sqlSelect,new ProductoExtractor());
	}
	public List<Producto> getTopProductos(){
		String sqlSelect="Select top 3 id_producto, nombre,existencias,precio,Producto.descripcion, "+
	    " minimo_existencias,Categoria.id_categoria,Categoria.descripcion_categoria"+
				" From Producto,Categoria";
		return jdbcTemplate.query(sqlSelect,new ProductoExtractor());
	}
	
	public List<Producto> getProductosPorCategoria(String categoria){
		String sqlSelect=" Select id_producto, nombre,existencias,precio,Producto.descripcion,minimo_existencias, "+
	" Categoria.id_categoria,Categoria.descripcion_categoria from Producto,Categoria " + 
	" where Producto.id_categoria=Categoria.id_categoria AND Categoria.descripcion_categoria="+"'"+categoria+"'";
		return jdbcTemplate.query(sqlSelect,new ProductoExtractor());
		
	}
	public List <Producto> getProductosPorPrecio(int precio){
		String sqlSelect=" Select id_producto, nombre,existencias,precio,Producto.descripcion,minimo_existencias, "+
				" Categoria.id_categoria,Categoria.descripcion_categoria from Producto,Categoria " + 
				" where Producto.id_categoria=Categoria.id_categoria AND Producto.precio="+"'"+precio+"'";
					return jdbcTemplate.query(sqlSelect,new ProductoExtractor());
	}
	public void deleteProducto(int idProducto) throws SQLException {
		
		 Connection connection=null;
	     try {
	    	 connection=dataSource.getConnection();
	 		connection.setAutoCommit(false);
	 		
	 		String sqlDelete= "Delete from Producto where id_producto=?";
	 		PreparedStatement statementProducto= connection.prepareStatement(sqlDelete);
	 		statementProducto.setInt(1,idProducto);
	 		statementProducto.executeUpdate();
	 		connection.commit();
	     } catch (SQLException e) {
	 		connection.rollback();
	 	    throw e;
	 	}finally {
	 		if(connection!=null) {
	 			connection.close();
	 		}
	 	}
	 	}
	     
		
		/*
		this.simpleJdbcCall.withProcedureName("VentasEnLinea_ProductoDelete");
		Map<String,Object> inParamMap= new HashMap<String,Object>();
		inParamMap.put("id_Producto", idProducto);
		SqlParameterSource in= new MapSqlParameterSource(inParamMap);
		simpleJdbcCall.execute(in);
		*/
	
	public Producto findByID(int idProducto) {
		System.out.println("Update "+idProducto);
		String sqlProcedure= "{call VentasEnLinea_ProductoGetById(?)}";
		Producto producto=this.jdbcTemplate.queryForObject(sqlProcedure, new ProductoRowMapper(),idProducto);
		return producto;
	}
	public void update (Producto producto) {
		this.simpleJdbcCall.withProcedureName("VentasEnLinea_ModificarProducto");
		Map<String,Object> inParamMap=new HashMap<String,Object>();
		inParamMap.put("id_producto", producto.getIdProducto());
		inParamMap.put("nombre", producto.getNombre());
		inParamMap.put("precio", producto.getPrecio());
		inParamMap.put("descripcion", producto.getDescripcion());
		inParamMap.put("minimo_existencias", producto.getMinimoExistencias());
		SqlParameterSource in=new MapSqlParameterSource(inParamMap);
		simpleJdbcCall.execute(in);
		
	}
	public Producto insert(Producto producto) throws SQLException {
		Connection connection = dataSource.getConnection();
		String sqlInsert = "{call VentasEnLinea_ProductoInsert(?,?,?,?,?,?,?)}";
		CallableStatement statement = connection.prepareCall(sqlInsert);
		statement.registerOutParameter(1, Types.INTEGER);
		statement.setString(2, producto.getNombre());
		statement.setInt(3, producto.getExistencias());
		statement.setFloat(4, producto.getPrecio());
		statement.setInt(5, producto.getMinimoExistencias());
		statement.setInt(6, producto.getCategoria().getIdCategoria());
		statement.setString(7, producto.getDescripcion());
		statement.execute();
		statement.close();
		connection.close();
		return producto;
	}
	class ProductoRowMapper implements RowMapper<Producto>{

		@Override
		public Producto mapRow(ResultSet rs, int rowNum) throws SQLException {
			Producto producto=new Producto();
			producto.setIdProducto(rs.getInt("id_producto"));
			producto.setNombre(rs.getString("nombre"));
			producto.setExistencias(rs.getInt("existencias"));
			producto.setPrecio(rs.getFloat("precio"));
			producto.setDescripcion(rs.getString("descripcion"));
			producto.setMinimoExistencias(rs.getInt("minimo_existencias"));
			producto.getCategoria().setIdCategoria(rs.getInt("id_categoria"));
			producto.getCategoria().setDescripcion(rs.getString("descripcion_categoria"));
			return producto;
		}
		
	}


	
	private static final class ProductoExtractor implements ResultSetExtractor<List<Producto>> {
	
		@Override
		public List<Producto> extractData(ResultSet rs) throws SQLException, DataAccessException {
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

				}//if
			} // while
			return new ArrayList<Producto>(map.values());
		}

	}
	
	}

