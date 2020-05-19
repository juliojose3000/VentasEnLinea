package com.ventasenlinea.ventas.dto;

import java.sql.CallableStatement;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.ventasenlinea.ventas.domain.CategoriaMasVendida;
import com.ventasenlinea.ventas.domain.ClienteLogueado;
import com.ventasenlinea.ventas.domain.Orden;
import com.ventasenlinea.ventas.domain.ReporteVenta;
import com.ventasenlinea.ventas.domain.Ventas;

@Repository
public class ReporteVentaDao {
	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;
	private SimpleJdbcCall simpleJdbcCall;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.simpleJdbcCall = new SimpleJdbcCall(dataSource);
	}

	public List<ReporteVenta> getReporteVenta(int idCliente) {

		String sqlSelect = "Select id_cliente, fecha_venta, id_orden,nombre_cliente,direccion,id_producto,nombre,cantidad,sub_total,"
				+ "impuesto,total from Reporte_venta where  id_cliente = " + idCliente;

		System.out.println("sqlSelect");

		return this.jdbcTemplate.query(sqlSelect, new ReporteExtractor());

	}

	public List<CategoriaMasVendida> getTopCategorias() {
		String sqlProcedure = "{call dos_productos_mas_vendidos_por_categoria}";
		return this.jdbcTemplate.query(sqlProcedure, new CategoriaTopExtractor());

	}

	public List<Ventas> getTopVentas() {
		String sqlProcedure = "{call VentasEnLinea_listadoTotalVentas}";
		return this.jdbcTemplate.query(sqlProcedure, new TotalVentasExtractor());
	}

	public ReporteVenta crearReporte(ClienteLogueado clienteLogueado) throws SQLException {
		Connection connection = dataSource.getConnection();
		String sqlInsert = "{call VentasEnLinea_ReporteOrdenInsert(?)}";
		CallableStatement statement = connection.prepareCall(sqlInsert);
		statement.setInt(1, clienteLogueado.getIdCliente());
		statement.execute();
		statement.close();
		connection.close();
		return new ReporteVenta();
	}

	private static final class TotalVentasExtractor implements ResultSetExtractor<List<Ventas>> {

		@Override
		public List<Ventas> extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<Float, Ventas> map = new HashMap<Float, Ventas>();
			Ventas totalVentas = null;
			while (rs.next()) {
				float total = rs.getFloat("Total");
				totalVentas = map.get(total);
			         if(totalVentas==null) {
					totalVentas = new Ventas();
                    totalVentas.setIdCliente(rs.getInt("id_cliente"));
                    totalVentas.setNombre(rs.getString("nombre"));
                    totalVentas.setApellidos(rs.getString("apellidos"));
                    totalVentas.setTotal(total);
                    
					map.put(total, totalVentas);
			         }
			}
			return new ArrayList<Ventas>(map.values());
		}

	}

	private static final class CategoriaTopExtractor implements ResultSetExtractor<List<CategoriaMasVendida>> {

		@Override
		public List<CategoriaMasVendida> extractData(ResultSet rs) throws SQLException, DataAccessException {
			CategoriaMasVendida categoriaTop = null;
			Map<Integer, CategoriaMasVendida> map = new HashMap<Integer, CategoriaMasVendida>();
			
			while (rs.next()) {
				Integer cantidadVentas = rs.getInt("Cantidad Ventas");
				categoriaTop = map.get(cantidadVentas);
				if (categoriaTop == null) {
					categoriaTop = new CategoriaMasVendida();
					categoriaTop.setCantidadVendidas(cantidadVentas);
					categoriaTop.setDescripcion(rs.getString("descripcion_categoria"));

					map.put(cantidadVentas, categoriaTop);
				}
			}
			return new ArrayList<CategoriaMasVendida>(map.values());
		}

	}

	private static final class ReporteExtractor implements ResultSetExtractor<List<ReporteVenta>> {

		@Override
		public List<ReporteVenta> extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<Integer, ReporteVenta> map = new HashMap<Integer, ReporteVenta>();
			ReporteVenta reporte = null;
			while (rs.next()) {
				Integer id = rs.getInt("id_orden");
				reporte = map.get(id);
				if (reporte == null) {
					reporte = new ReporteVenta();
					reporte.setFecha(rs.getDate("fecha_venta"));
					reporte.setNumeroOrden(id);
					reporte.setNombreCliente(rs.getString("nombre_cliente"));
					reporte.setDireccion(rs.getString("direccion"));
					reporte.setIdProducto(rs.getInt("id_producto"));
					reporte.setNombreProducto(rs.getString("nombre"));
					reporte.setCantidadProductos(rs.getInt("cantidad"));
					reporte.setSubTotal(rs.getFloat("sub_total"));
					reporte.setImpuesto(rs.getInt("impuesto"));
					reporte.setTotalOrden(rs.getFloat("total"));
					map.put(id, reporte);
				}
			}

			return new ArrayList<ReporteVenta>(map.values());
		}

	}

	/*
	 * class ReporteRowMapper implements RowMapper<ReporteVenta>{
	 * 
	 * @Override public ReporteVenta mapRow(ResultSet rs, int rowNum) throws
	 * SQLException { ReporteVenta reporte = new ReporteVenta();
	 * reporte.setIdCliente(rs.getInt("id_cliente"));
	 * reporte.setNombre(rs.getString("nombre"));
	 * reporte.setApellidos(rs.getString("apellidos"));
	 * reporte.setNumeroOrden(rs.getInt("numero_orden"));
	 * reporte.setFecha(rs.getDate("fecha_orden"));
	 * reporte.setCantidadProductos(rs.getInt("cantidad"));
	 * reporte.setTotalOrden(rs.getFloat("subtotal_orden"));
	 * 
	 * return reporte; }
	 * 
	 * }
	 */
}
