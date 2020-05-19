package com.ventasenlinea.ventas.domain;

public class Carrito {
	
	private int consecutivo;
	
	private int idCliente;
	
	private int idProducto;
	
	private String nombre;
	
	private int cantidad;
	
	private float precio;
	
	

	public Carrito() {

	}
	
	

	public Carrito(int consecutivo, int idCliente, int idProducto, String nombre, int cantidad, float precio) {

		this.consecutivo = consecutivo;
		this.idCliente = idCliente;
		this.idProducto = idProducto;
		this.nombre = nombre;
		this.cantidad = cantidad;
		this.precio = precio;
	}



	public int getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public int getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(int idProducto) {
		this.idProducto = idProducto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	@Override
	public String toString() {
		return "Carrito [idCliente=" + idCliente + ", idProducto=" + idProducto + ", nombre=" + nombre + ", cantidad="
				+ cantidad + ", precio=" + precio + "]";
	}
	
	
	

}

