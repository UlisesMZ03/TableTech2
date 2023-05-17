
import java.io.Serializable;

public class Platillo implements Comparable<Platillo>, Serializable  {
    private String nombre;
    private int calorias;
    private int tiempoPreparacion; // en minutos
    private double precio;

    // Constructor
    public Platillo(String nombre, int calorias, int tiempoPreparacion, double precio) {
        this.nombre = nombre;
        this.calorias = calorias;
        this.tiempoPreparacion = tiempoPreparacion;
        this.precio = precio;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCalorias() {
        return calorias;
    }

    public void setCalorias(int calorias) {
        this.calorias = calorias;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public void setTiempoPreparacion(int tiempoPreparacion) {
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    // toString para imprimir información del platillo
    @Override
    public String toString() {
        return nombre + " - " + calorias + " calorías - " + tiempoPreparacion + " minutos - $" + precio;
    }

    // Método compareTo para comparar platillos por nombre
    @Override
    public int compareTo(Platillo o) {
        return nombre.compareTo(o.getNombre());
    }
}
