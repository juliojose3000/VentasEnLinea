package com.ventasenlinea.ventas.domain;

import java.sql.Date;

public class ReporteVenta {
	private Date fecha;
	private int numeroOrden;
	private String nombreCliente;
    private String direccion;
    private int idProducto;
    private String nombreProducto;
	private int cantidadProductos;
    private float subTotal;
    private int impuesto;
	private float totalOrden;

	public ReporteVenta() {

	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getNumeroOrden() {
		return numeroOrden;
	}

	public void setNumeroOrden(int numeroOrden) {
		this.numeroOrden = numeroOrden;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public int getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(int idProducto) {
		this.idProducto = idProducto;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public int getCantidadProductos() {
		return cantidadProductos;
	}

	public void setCantidadProductos(int cantidadProductos) {
		this.cantidadProductos = cantidadProductos;
	}

	public float getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(float subTotal) {
		this.subTotal = subTotal;
	}

	public int getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(int impuesto) {
		this.impuesto = impuesto;
	}

	public float getTotalOrden() {
		return totalOrden;
	}

	public void setTotalOrden(float totalOrden) {
		this.totalOrden = totalOrden;
	}

	@Override
	public String toString() {
		return "ReporteVenta [fecha=" + fecha + ", numeroOrden=" + numeroOrden + ", nombreCliente=" + nombreCliente
				+ ", direccion=" + direccion + ", idProducto=" + idProducto + ", nombreProducto=" + nombreProducto
				+ ", cantidadProductos=" + cantidadProductos + ", subTotal=" + subTotal + ", impuesto=" + impuesto
				+ ", totalOrden=" + totalOrden + "]";
	}


}
