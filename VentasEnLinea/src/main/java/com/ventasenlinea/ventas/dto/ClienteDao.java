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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ventasenlinea.ventas.domain.Cliente;
import com.ventasenlinea.ventas.domain.ContactoCliente;
import com.ventasenlinea.ventas.domain.DireccionCliente;

@Repository
public class ClienteDao {
	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public Cliente insertarCliente(Cliente cliente) throws SQLException {
		Connection connection = dataSource.getConnection();

		String sqlInsert = "{call VentasEnLinea_RegistarCliente(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement statement = connection.prepareCall(sqlInsert);
		statement.setInt(1, cliente.getIdCliente());
		statement.setString(2, cliente.getNombre());
		statement.setString(2, cliente.getNombre());
		statement.setString(3, cliente.getApellidos());
		statement.setString(4, cliente.getCorreo());
		statement.setString(5, cliente.getPassword());
		statement.setInt(6, cliente.getDireccion().getIdDireccion());
		statement.setString(7, cliente.getDireccion().getDireccion1());
		statement.setString(8, cliente.getDireccion().getDireccion2());
		statement.setString(9, cliente.getDireccion().getPais());
		statement.setString(10, cliente.getDireccion().getProvincia());
		statement.setString(11, cliente.getDireccion().getCanton());
		statement.setInt(12, cliente.getDireccion().getCodigoPostal());
		statement.setInt(13, cliente.getContactoCliente().getInformacionContacto());
		statement.setString(14, cliente.getContactoCliente().getTelefono1());
		statement.setString(15, cliente.getContactoCliente().getTelefono2());

		statement.execute();
		statement.close();

		connection.close();

		return cliente;
	}

	/*
	 * public ContactoCliente insertarContactoCliente(ContactoCliente
	 * contactoCliente) throws SQLException { Connection
	 * connection=dataSource.getConnection(); String sqlInsert3=
	 * "{call VentasEnLinea_RegistarInformacionContactoCliente(?,?,?,?)}";
	 * CallableStatement statement3= connection.prepareCall(sqlInsert3);
	 * statement3.registerOutParameter(1, Types.INTEGER);
	 * statement3.setString(2,contactoCliente.getTelefono1());
	 * statement3.setString(3, contactoCliente.getTelefono2()); statement3.setInt(4,
	 * contactoCliente.getCliente().getIdCliente()); statement3.execute();
	 * statement3.close(); return contactoCliente; }
	 * 
	 * 
	 * public DireccionCliente insertarDireccionCliente(DireccionCliente direccion)
	 * throws SQLException { Connection connection=dataSource.getConnection();
	 * String sqlInsert2=
	 * "{call VentasEnLinea_RegistarDireccionCliente(?,?,?,?,?,?,?,?)}";
	 * CallableStatement statement2= connection.prepareCall(sqlInsert2);
	 * statement2.registerOutParameter(1, Types.INTEGER); statement2.setString(2,
	 * direccion.getDireccion1());
	 * statement2.setString(3,direccion.getDireccion2());
	 * statement2.setString(4,direccion.getPais()); statement2.setString(5,
	 * direccion.getProvincia()); statement2.setString(6, direccion.getCanton());
	 * statement2.setInt(7, direccion.getCodigoPostal()); statement2.setInt(8,
	 * direccion.getCliente().getIdCliente()); statement2.execute();
	 * statement2.close(); connection.close(); return direccion; }
	 */
	public Cliente findByEmailAndPassword(String correo, String password) {
		String sqlProcedure = "{call VentasEnLinea_ClienteCorreoPassword(?,?)}";
		Cliente cliente;
		try {
			cliente = this.jdbcTemplate.queryForObject(sqlProcedure, new ClienteCompletoRowMapper(), correo, password);
		} catch (EmptyResultDataAccessException e) {
			return new Cliente();
		}
		System.out.println(cliente.toString());
		return cliente;
	}

	public Cliente getClienteCompleto(int idCliente) {
		String sqlProcedure = "{call VentasEnLinea_ClienteCompleto(?)}";
		Cliente cliente;
		try {
			cliente = this.jdbcTemplate.queryForObject(sqlProcedure, new ClienteCompletoRowMapper(), idCliente);
		} catch (EmptyResultDataAccessException e) {
			return new Cliente();
		}
		return cliente;

	}
	public List<Cliente>getAll(){
		String sqlSelect = "Select Cliente.id_cliente,nombre,apellidos,correo,password,d.id_direccion,d.direccion1,d.direccion2,"
				+ "d.pais,d.provincia,d.canton,d.codigo_postal, i.informacion_contacto_id,i.phone,i.phone,i.phone2 "+
				"  from Cliente, Direccion d, Informacion_Contacto_Cliente i";
		return jdbcTemplate.query(sqlSelect, new ClienteExtractor());
	}

	private static final class ClienteExtractor implements ResultSetExtractor<List<Cliente>>{
		
		
		
		@Override
		public List<com.ventasenlinea.ventas.domain.Cliente> extractData(ResultSet rs)
				throws SQLException, DataAccessException {
			Map<Integer,Cliente> map= new HashMap<Integer,Cliente>();
			Cliente cliente=null;
			while(rs.next()) {
				Integer id= rs.getInt("id_cliente");
				cliente= map.get(id);
				if(cliente==null) {
					cliente=new Cliente();
					 cliente.setIdCliente(id);
			         cliente.setNombre(rs.getString("nombre"));
			         cliente.setApellidos(rs.getString("apellidos"));
			         cliente.setCorreo(rs.getString("correo"));
			         cliente.setPassword(rs.getString("password"));
			         cliente.getDireccion().setIdDireccion(rs.getInt("id_direccion"));
			         cliente.getDireccion().setDireccion1(rs.getString("direccion1"));
			         cliente.getDireccion().setDireccion2(rs.getString("direccion2"));
			         cliente.getDireccion().setPais(rs.getString("pais"));
			         cliente.getDireccion().setProvincia("provincia");
			         cliente.getDireccion().setCanton(rs.getString("canton"));
			         cliente.getDireccion().setCodigoPostal(rs.getInt("codigo_postal"));
			         cliente.getContactoCliente().setInformacionContacto(rs.getInt("informacion_contacto_id"));
			         cliente.getContactoCliente().setTelefono1(rs.getString("phone"));
			         cliente.getContactoCliente().setTelefono2(rs.getString("phone2"));
			         map.put(id,cliente);
				}//if
				
				
		}//while
			return new ArrayList<Cliente>(map.values());
	}
	}
	
	class ClienteCompletoRowMapper implements RowMapper<Cliente>{

		@Override
		public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
            Cliente cliente= new Cliente();
            cliente.setIdCliente(rs.getInt("id_cliente"));
            cliente.setNombre(rs.getString("nombre"));
            cliente.setApellidos(rs.getString("apellidos"));
            cliente.setCorreo(rs.getString("correo"));
            cliente.setPassword(rs.getString("password"));
            cliente.getDireccion().setIdDireccion(rs.getInt("id_direccion"));
            cliente.getDireccion().setDireccion1(rs.getString("direccion1"));
            cliente.getDireccion().setDireccion2(rs.getString("direccion2"));
            cliente.getDireccion().setPais(rs.getString("pais"));
            cliente.getDireccion().setProvincia("provincia");
            cliente.getDireccion().setCanton(rs.getString("canton"));
            cliente.getDireccion().setCodigoPostal(rs.getInt("codigo_postal"));
            cliente.getContactoCliente().setInformacionContacto(rs.getInt("informacion_contacto_id"));
            cliente.getContactoCliente().setTelefono1(rs.getString("phone"));
            cliente.getContactoCliente().setTelefono2(rs.getString("phone2"));
            
             
			return cliente;
		}
		
	}
	class ClienteRowMapper implements RowMapper<Cliente>{

		@Override
		public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
            Cliente cliente= new Cliente();
            cliente.setIdCliente(rs.getInt("id_cliente"));
            cliente.setNombre(rs.getString("nombre"));
            cliente.setApellidos(rs.getString("apellidos"));
            cliente.setCorreo(rs.getString("correo"));
            cliente.setPassword(rs.getString("password"));
            
			return cliente;
		}
		
	}
}
