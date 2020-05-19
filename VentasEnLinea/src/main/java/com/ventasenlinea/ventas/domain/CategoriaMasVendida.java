package com.ventasenlinea.ventas.domain;

public class CategoriaMasVendida {

	public String descripcion;
	public int cantidadVendidas;
	
	public CategoriaMasVendida() {
		
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getCantidadVendidas() {
		return cantidadVendidas;
	}

	public void setCantidadVendidas(int cantidadVendidas) {
		this.cantidadVendidas = cantidadVendidas;
	}

	@Override
	public String toString() {
		return "CategoriaMasVendida [descripcion=" + descripcion + ", cantidadVendidas=" + cantidadVendidas + "]";
	}
	
}
