package com.ventasenlinea.ventas.dto;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.ventasenlinea.ventas.domain.Cliente;
import com.ventasenlinea.ventas.domain.ClienteLogueado;
@Repository
public class ClienteLogueadoDao {
	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;
	private SimpleJdbcCall simpleJdbcCall;
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.simpleJdbcCall=new SimpleJdbcCall(dataSource);
	}
	public ClienteLogueado insertarClienteLogueado(ClienteLogueado cliente) throws SQLException {
		Connection connection = dataSource.getConnection();
		String sqlInsert = "{call VentasEnLinea_RegistrarClienteLogueado(?)}";
		CallableStatement statement = connection.prepareCall(sqlInsert);
		statement.setInt(1, cliente.getIdCliente());
		statement.execute();
		statement.close();

		connection.close();
		
		return cliente;

	}
	public void deleteClienteLogueado() {
		this.simpleJdbcCall.withProcedureName("VentasEnLinea_ClienteLogueadoDelete");
		Map<String,Object> inParamMap= new HashMap<String,Object>();
		
		SqlParameterSource in= new MapSqlParameterSource(inParamMap);
		simpleJdbcCall.execute(in);
	}
	
	public ClienteLogueado getClienteLogueado() {
		String sqlProcedure = "{call VentasEnLinea_GetClienteLogueado}";
		
		ClienteLogueado clienteLogueado;
		try {
			clienteLogueado = this.jdbcTemplate.queryForObject(sqlProcedure, new ClienteLogueadoRowMapper());
		} catch (EmptyResultDataAccessException e) {
			return new ClienteLogueado();
		}
		return clienteLogueado;
//
	}
	
	class ClienteLogueadoRowMapper implements RowMapper<ClienteLogueado>{

		@Override
		public ClienteLogueado mapRow(ResultSet rs, int rowNum) throws SQLException {
			ClienteLogueado cliente= new ClienteLogueado();
            cliente.setIdCliente(rs.getInt("id_cliente"));
           
            
             
			return cliente;
		}
		
	}
	
}
