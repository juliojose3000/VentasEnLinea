package com.ventasenlinea.ventas.dto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ventasenlinea.ventas.domain.Encargado;


@Repository
public class EncargadoDao {

	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public Encargado findByEmailAndPassword (String correo,String password) {
		String sqlProcedure= "{call VentasEnLinea_EncargadoCorreoPassword(?,?)}";
		Encargado encargado;
		try {
		 encargado=this.jdbcTemplate.queryForObject(sqlProcedure, new GeneroRowMapper(),correo,password);
		
		}catch(EmptyResultDataAccessException e) {
		return new Encargado();
		}
		
		return encargado;
		
	}
	class GeneroRowMapper implements RowMapper<Encargado>{

		@Override
		public Encargado mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			Encargado encargado= new Encargado();
			
			encargado.setId(rs.getInt("id"));
			encargado.setDni(rs.getInt("dni"));
			encargado.setNombre(rs.getString("nombre"));
			encargado.setApellidos(rs.getString("apellidos"));
			encargado.setCorreo(rs.getString("correo"));
			encargado.setPassword(rs.getString("password"));
			encargado.getRol().setId((rs.getInt("id_rol")));
			encargado.getRol().setTipoRol((rs.getString("tipo_rol")));
			
			return encargado;
		}
	}
	
	public List<Encargado> inicioSesion(String correo, String password) {
			String sqlSelect = "SELECT id,nombre,apellidos,dni, correo,password,r.id_rol,r.tipo_rol"
					+ " FROM Encargado,Rol r "
					+ "where r.id_rol =Encargado.id_rol AND correo = '"+correo.trim()+"' and password= '"+password+"'";
				
				
		return jdbcTemplate.query(sqlSelect, new EncargadoExtractor());

	}
	
	public List<Encargado> getAll() {
		String sqlSelect = "SELECT id,nombre,apellidos,dni, correo,password,r.id_rol,r.tipo_rol"
				+ " FROM Encargado,Rol r "
				+ "where r.id_rol =Encargado.id_rol ";
			
			
	return jdbcTemplate.query(sqlSelect, new EncargadoExtractor());

}

	private static final class EncargadoExtractor implements ResultSetExtractor<List<Encargado>> {

		@Override
		public List<Encargado> extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<Integer, Encargado> map = new HashMap<Integer, Encargado>();
			Encargado encargado = null;
			while (rs.next()) {
				Integer id = rs.getInt("id");
				encargado = map.get(id);
				if (encargado == null) {
					encargado = new Encargado();
					encargado.setId(id);
					encargado.setNombre(rs.getString("nombre"));
					encargado.setApellidos(rs.getString("apellidos"));
					encargado.setDni(rs.getInt("dni"));
					encargado.setCorreo(rs.getString("correo"));
					encargado.setPassword(rs.getString("password"));
					encargado.getRol().setId(rs.getInt("id_rol"));
					encargado.getRol().setTipoRol(rs.getString("tipo_rol"));
					
					map.put(id, encargado);
				} // if
				
			} // while
			return new ArrayList<Encargado>(map.values());
		}

	}
}
