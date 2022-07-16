package com.dantsu.mcf;

public class ModeloCliente {

    private int id;
    private String nombre;
    private String direccion;
    private int telefono;

    public ModeloCliente(int id, String nombre, String direccion, int telefono) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }
    public ModeloCliente() {
    }

    @Override
    public String toString() {
        return "ModeloCliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono=" + telefono +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTelefono() {
        return telefono;
    }

    public String getTelefonoString(){return Integer.toString(telefono);}

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }


    public boolean completoCliente(){

        if(this.nombre.isEmpty() || this.direccion.isEmpty()){
            return false;
        }

        if(this.getTelefonoString().length() < 9){
            return false;
        }

        return true;



    }
}
