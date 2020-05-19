package com.ventasenlinea.ventas.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

public class ContactoCliente {


private int informacionContacto;



private String telefono1;


private String telefono2;



public ContactoCliente() {
	
}






public int getInformacionContacto() {
	return informacionContacto;
}

public void setInformacionContacto(int informacionContacto) {
	this.informacionContacto = informacionContacto;
}

public String getTelefono1() {
	return telefono1;
}

public void setTelefono1(String telefono1) {
	this.telefono1 = telefono1;
}

public String getTelefono2() {
	return telefono2;
}

public void setTelefono2(String telefono2) {
	this.telefono2 = telefono2;
}



@Override
public String toString() {
	return "ContactoCliente [informacionContacto=" + informacionContacto + ", telefono1=" + telefono1 + ", telefono2="
			+ telefono2 + "]";
}



}
