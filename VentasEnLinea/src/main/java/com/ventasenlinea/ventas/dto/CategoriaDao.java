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
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.ventasenlinea.ventas.domain.Categoria;
import com.ventasenlinea.ventas.domain.Producto;

@Repository
public class CategoriaDao {
	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;
	private SimpleJdbcCall simpleJdbcCall;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.simpleJdbcCall=new SimpleJdbcCall(dataSource);
	}
	
	public List<Categoria> getAllCategorias(){
		String sqlSelect= "Select id_categoria,descripcion_categoria from Categoria";
		return jdbcTemplate.query(sqlSelect, new ProductoExtractor());
		
	}
	public Categoria FindByID(int idCategoria) {
		String sqlProcedure="{call VentasEnLinea_CategoriaGetById(?)}";
		Categoria categoria=this.jdbcTemplate.queryForObject(sqlProcedure, new CategoriaRowMapper(),idCategoria);
		return categoria;
	}
	public Categoria findByDescripcion(String descripcion) {
		String sqlProcedure="{call VentasEnLinea_CategoriaGetByDescripcion(?)}";
		Categoria categoria= this.jdbcTemplate.queryForObject(sqlProcedure, new CategoriaRowMapper(),descripcion);
		return categoria;
	}
	
	public void deleteCategoria(int idCategoria) throws SQLException {
		 Connection connection=null;
	     try {
	    	 connection=dataSource.getConnection();
	 		connection.setAutoCommit(false);
	 		
	 		String sqlDelete= "Delete from Categoria where id_categoria=?";
	 		PreparedStatement statementProducto= connection.prepareStatement(sqlDelete);
	 		statementProducto.setInt(1,idCategoria);
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
	
	public void update(Categoria categoria) {
		this.simpleJdbcCall.withProcedureName("VentasEnLinea_ModificarCategoria");	
		Map<String,Object> inParamMap=new HashMap<String,Object>();
		inParamMap.put("idCategoria", categoria.getIdCategoria());
		inParamMap.put("descripcion",categoria.getDescripcion());
		SqlParameterSource in=new MapSqlParameterSource(inParamMap);
		simpleJdbcCall.execute(in);
		
		
	}
	public Categoria insert(Categoria categoria) throws SQLException {
		Connection connection = dataSource.getConnection();
		String sqlInsert = "{call VentasEnLinea_CategoriaInsert(?,?)}";
		CallableStatement statement = connection.prepareCall(sqlInsert);
		statement.registerOutParameter(1, Types.INTEGER);
		statement.setString(2, categoria.getDescripcion());
		statement.execute();
		categoria.setIdCategoria(statement.getInt("idCategoria"));
		statement.close();
		connection.close();
		return categoria;
		
		
		
	}
	
	
	private static final class ProductoExtractor implements ResultSetExtractor<List<Categoria>>{

		@Override
		public List<Categoria> extractData(ResultSet rs) throws SQLException, DataAccessException {
		 Map<Integer,Categoria> map= new HashMap<Integer,Categoria>();
		 Categoria  categoria=null;
		 while (rs.next()) {
			 Integer id= rs.getInt("id_categoria");
			 categoria=map.get(id);
			 if(categoria==null) {
				 categoria=new Categoria();
				 categoria.setIdCategoria(id);
				 categoria.setDescripcion(rs.getString("descripcion_categoria"));
				 map.put(id, categoria); 
			 }//if
			
		 }//while
		 return new ArrayList<Categoria>(map.values());
		}
		
		
			
		}
	class CategoriaRowMapper implements RowMapper<Categoria>{

		@Override
		public Categoria mapRow(ResultSet rs, int rowNum) throws SQLException {
			Categoria categoria=new Categoria();
			categoria.setIdCategoria(rs.getInt("id_categoria"));
			categoria.setDescripcion(rs.getString("descripcion_categoria"));
			return categoria;
		}
		
	}
	

}
