package com.ventasenlinea.ventas.domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


public class Orden {

	private int numeroOrden;
	private Date fechaOrden;
	private int idCliente;
	private Cliente cliente;
	private List<DetalleOrden> listaDetalles;

	public Orden() {
		this.listaDetalles = new ArrayList<>();
	}

	

	public Orden(int numeroOrden, Date fechaOrden, int idCliente, Cliente cliente, List<DetalleOrden> listaDetalles) {
		this.numeroOrden = numeroOrden;
		this.fechaOrden = fechaOrden;
		this.idCliente = idCliente;
		this.cliente = cliente;
		this.listaDetalles = listaDetalles;
	}



	@Override
	public String toString() {
		return "Orden [numeroOrden=" + numeroOrden + ", fechaOrden=" + fechaOrden + ", idCliente=" + idCliente
				+ ", cliente=" + cliente + ", listaDetalles=" + listaDetalles + "]";
	}



	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public int getNumeroOrden() {
		return numeroOrden;
	}

	public void setNumeroOrden(int numeroOrden) {
		this.numeroOrden = numeroOrden;
	}

	public Date getFechaOrden() {
		return fechaOrden;
	}

	public void setFechaOrden(Date fechaOrden) {
		this.fechaOrden = fechaOrden;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<DetalleOrden> getListaDetalles() {
		return listaDetalles;
	}

	public void setListaDetalles(List<DetalleOrden> listaDetalles) {
		this.listaDetalles = listaDetalles;
	}

	public void setDetalleOrden(DetalleOrden detalleOrden) {
		this.listaDetalles.add(detalleOrden);		
	}


}

