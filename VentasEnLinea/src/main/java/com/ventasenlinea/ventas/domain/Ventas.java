package com.ventasenlinea.ventas.domain;

public class Ventas {
	
private int idCliente;
private String nombre;
private String apellidos;
private float total;

public Ventas() {
	
}
public int getIdCliente() {
	return idCliente;
}
public void setIdCliente(int idCliente) {
	this.idCliente = idCliente;
}
public String getNombre() {
	return nombre;
}
public void setNombre(String nombre) {
	this.nombre = nombre;
}
public String getApellidos() {
	return apellidos;
}
public void setApellidos(String apellidos) {
	this.apellidos = apellidos;
}
public float getTotal() {
	return total;
}
public void setTotal(float total) {
	this.total = total;
}
@Override
public String toString() {
	return "Ventas [idCliente=" + idCliente + ", nombre=" + nombre + ", apellidos=" + apellidos + ", total=" + total
			+ "]";
}



}
