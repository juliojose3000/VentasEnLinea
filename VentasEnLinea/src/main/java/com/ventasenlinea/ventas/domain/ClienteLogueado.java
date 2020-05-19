package com.ventasenlinea.ventas.domain;

public class ClienteLogueado {
private int idCliente;

public ClienteLogueado() {

}

public int getIdCliente() {
	return idCliente;
}

public void setIdCliente(int idCliente) {
	this.idCliente = idCliente;
}

@Override
public String toString() {
	return "Cliente [idCliente=" + idCliente + "]";
}


}
