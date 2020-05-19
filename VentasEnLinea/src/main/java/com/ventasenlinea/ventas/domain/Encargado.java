package com.ventasenlinea.ventas.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "dbo.Encargado")
public class Encargado {

	@Id
 @GeneratedValue(strategy=GenerationType.IDENTITY)
@Column(name="id")
private int id;

@Column(name="dni")
private int dni;

@Column(name="nombre")
private String nombre;

@Column(name="apellidos")
private String apellidos;

@Column(name="correo")
private String correo;

@Column(name="password")
private String password;

 @ManyToOne
 @JoinColumn(name="id_rol")
private Rol rol;

public Encargado() {
	this.rol=new Rol();
}



public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public int getDni() {
	return dni;
}

public void setDni(int dni) {
	this.dni = dni;
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

public Rol getRol() {
	return rol;
}

public void setRol(Rol rol) {
	this.rol = rol;
}

@Override
public String toString() {
	return "Encargado [id=" + id + ", dni=" + dni + ", nombre=" + nombre + ", apellidos=" + apellidos + ", correo="
			+ correo + ", password=" + password + ", rol=" + rol + "]";
}


}
