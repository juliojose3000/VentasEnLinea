package com.ventasenlinea.ventas.domain;

public class Cliente {
private int idCliente;
private String nombre;
private String apellidos;
private String correo;
private String password;
private DireccionCliente direccion;
private ContactoCliente contactoCliente;


public Cliente() {
	this.direccion=new DireccionCliente();
	this.contactoCliente=new ContactoCliente();
}



public DireccionCliente getDireccion() {
	return direccion;
}



public void setDireccion(DireccionCliente direccion) {
	this.direccion = direccion;
}



public ContactoCliente getContactoCliente() {
	return contactoCliente;
}



public void setContactoCliente(ContactoCliente contactoCliente) {
	this.contactoCliente = contactoCliente;
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

public String getCorreo() {
	return correo;
}

public void setCorreo(String correo) {
	this.correo = correo;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}



@Override
public String toString() {
	return "Cliente [idCliente=" + idCliente + ", nombre=" + nombre + ", apellidos=" + apellidos + ", correo=" + correo
			+ ", password=" + password + ", direccion=" + direccion.toString() + ", contactoCliente=" + contactoCliente.toString() + "]";
}






	
	
}
