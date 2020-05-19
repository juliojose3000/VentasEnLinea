package com.ventasenlinea.ventas.domain;

import java.awt.Image;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


public class Imagen {


private int imagenId;


private Image imagen_producto;
//chequear compatibilidad


private Producto producto;

public Imagen() {
	
}

public Imagen(int imagenId, Image imagen_producto, Producto producto) {
	
	this.imagenId = imagenId;
	this.imagen_producto = imagen_producto;
	this.producto = producto;
}

public int getImagenId() {
	return imagenId;
}

public void setImagenId(int imagenId) {
	this.imagenId = imagenId;
}

public Image getImagen_producto() {
	return imagen_producto;
}

public void setImagen_producto(Image imagen_producto) {
	this.imagen_producto = imagen_producto;
}

public Producto getProducto() {
	return producto;
}

public void setProducto(Producto producto) {
	this.producto = producto;
}

@Override
public String toString() {
	return "Imagen [imagenId=" + imagenId + ", imagen_producto=" + imagen_producto + ", producto=" + producto + "]";
}



}
