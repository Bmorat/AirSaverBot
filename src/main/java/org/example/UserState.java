package org.example;

public class UserState {
    private String origen;
    private String destino;
    private String fecha;
    private int estado = 0; // 0 = espera nada, 1 = espera origen, 2 = espera destino, 3 = espera fecha

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
}