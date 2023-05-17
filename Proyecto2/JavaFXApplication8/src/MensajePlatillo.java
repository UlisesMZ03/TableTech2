
import java.io.Serializable;

public class MensajePlatillo implements Serializable {
    private String accion; // "register" o "login"
    private String NombrePlatillo;
    private int calorias;
    private int tiempoPrep;
    private int precio;

    public MensajePlatillo(String accion, String NombrePlatillo, int Calorias, int TiempoPrep, int Precio) {
        this.accion = accion;
        this.NombrePlatillo = NombrePlatillo;
        this.calorias = Calorias;
        this.tiempoPrep = TiempoPrep;
        this.precio = Precio;
    }

    public String getAccion() {
        return accion;
    }

    public String getNombrePlatillo() {
        return NombrePlatillo;
    }

    public int getCalorias() {
        return calorias;
    }
    public int getTiempoPrep() {
        return tiempoPrep;
    }
    public int getPrecio() {
        return precio;
    }
}
