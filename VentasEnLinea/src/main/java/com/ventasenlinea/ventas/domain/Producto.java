package com.ventasenlinea.ventas.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



public class Producto {


	private int idProducto;
	
	
	private String nombre;
	

	private Categoria categoria;
	

    private int existencias;
    

    private float precio;
    

    private String descripcion;

    private int minimoExistencias;

	
    
    public Producto() {
    	categoria=new Categoria();
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



	public Categoria getCategoria() {
		return categoria;
	}



	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}



	public int getExistencias() {
		return existencias;
	}



	public void setExistencias(int existencias) {
		this.existencias = existencias;
	}



	public float getPrecio() {
		return precio;
	}



	public void setPrecio(float precio) {
		this.precio = precio;
	}



	public String getDescripcion() {
		return descripcion;
	}



	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



	public int getMinimoExistencias() {
		return minimoExistencias;
	}



	public void setMinimoExistencias(int minimoExistencias) {
		this.minimoExistencias = minimoExistencias;
	}







	@Override
	public String toString() {
		return "Producto [idProducto=" + idProducto + ", nombre=" + nombre + ", categoria=" + categoria
				+ ", existencias=" + existencias + ", precio=" + precio + ", descripcion=" + descripcion
				+ ", minimoExistencias=" + minimoExistencias + "]";
	}



	
    
    
	
}
