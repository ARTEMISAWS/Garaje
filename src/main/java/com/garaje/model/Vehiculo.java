
package com.garaje.model;
public class Vehiculo {
    private int id;
    private String placa;
    private String marca;
    private String modelo;
    private String color;
    private String propietario;
    private String año;

    
    public Vehiculo() { }

    
    public Vehiculo(int id, String placa, String marca, String modelo,
String color, String propietario) {
        this.id = id;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color; 
 

        this.propietario = propietario;
    }

    // Getters y setters 
   
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

 
    public String getPlaca() { return placa; }

    
    public void setPlaca(String placa) { this.placa = placa; }

    
    public String getMarca() { return marca; }

    
    public void setMarca(String marca) { this.marca = marca; }

   
    public String getModelo() { return modelo; }

    
    public void setModelo(String modelo) { this.modelo = modelo; }

    
    public String getColor() { return color; }

    /** @param color establece el color */
    public void setColor(String color) { this.color = color; }

    /** @return propietario del vehículo */
    public String getPropietario() { return propietario; }
    
    /** @param propietario establece el propietario */
    public void setPropietario(String propietario) { this.propietario =
propietario; }
}
 