package com.ventasenlinea.ventas.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


public class DetalleOrden {

	private Producto producto;
	private int cantidad;
	private float subTotalOrden;
	private float precio;

	public DetalleOrden() {

	}

	public DetalleOrden(Producto producto, int cantidad) {
		this.producto = producto;
		this.cantidad = cantidad;
		this.precio = (float) (producto.getPrecio()+(producto.getPrecio()*0.13));
		this.subTotalOrden = this.precio * this.cantidad;

	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public float getSubTotalOrden() {
		return subTotalOrden;
	}

	public void setSubTotalOrden(float subTotalOrden) {
		this.subTotalOrden = subTotalOrden;
	}

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	@Override
	public String toString() {
		return "DetalleOrden [producto=" + producto + ", cantidad=" + cantidad + ", subTotalOrden=" + subTotalOrden
				+ ", precio=" + precio + "]";
	}
	
	

}
