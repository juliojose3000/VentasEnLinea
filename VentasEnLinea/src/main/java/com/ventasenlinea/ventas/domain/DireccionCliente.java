package com.ventasenlinea.ventas.domain;




public class DireccionCliente {


private int idDireccion;


private String direccion1;


private String direccion2;


private String pais;


private String provincia;

private String canton;

private int codigoPostal;




public DireccionCliente() {
	
}






public String getCanton() {
	return canton;
}






public void setCanton(String canton) {
	this.canton = canton;
}






public int getIdDireccion() {
	return idDireccion;
}



public void setIdDireccion(int idDireccion) {
	this.idDireccion = idDireccion;
}



public String getDireccion1() {
	return direccion1;
}



public void setDireccion1(String direccion1) {
	this.direccion1 = direccion1;
}



public String getDireccion2() {
	return direccion2;
}



public void setDireccion2(String direccion2) {
	this.direccion2 = direccion2;
}



public String getPais() {
	return pais;
}



public void setPais(String pais) {
	this.pais = pais;
}



public String getProvincia() {
	return provincia;
}



public void setProvincia(String provincia) {
	this.provincia = provincia;
}



public int getCodigoPostal() {
	return codigoPostal;
}



public void setCodigoPostal(int codigoPostal) {
	this.codigoPostal = codigoPostal;
}






@Override
public String toString() {
	return "DireccionCliente [idDireccion=" + idDireccion + ", direccion1=" + direccion1 + ", direccion2=" + direccion2
			+ ", pais=" + pais + ", provincia=" + provincia + ", codigoPostal=" + codigoPostal + "]";
}



}
