package com.dantsu.mcf;

public class ModeloPedido {

    private int telefono;
    private String tipo;
    private double precio;
    private String metodoPago;
    private String fecha;
    private int id;

    public ModeloPedido(int id,int telefono,String tipo,double precio,String metodoPago,String fecha){

        this.telefono = telefono;
        this.tipo = tipo;
        this.precio = precio;
        this.metodoPago = metodoPago;
        this.id = id;
        this.fecha = fecha;
    }

    public ModeloPedido(){

    }


    @Override
    public String toString() {
        return "ModeloPedido{" +
                "telefono=" + telefono +
                ", tipo='" + tipo + '\'' +
                ", precio=" + precio +
                ", fecha='" + fecha + '\'' +
                ", metodoPago='" + metodoPago + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}
