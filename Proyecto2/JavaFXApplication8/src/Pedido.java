
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Pedido implements Serializable {

   // private static int contadorPedidos = 1; // Contador de pedidos para generar números de orden únicos
    private String nombrePedido;
    private int tiempoTotal; // en minutos
    private int tiempoRestante;
    private List<Platillo> platillos;
    private String numeroOrden;

    public Pedido(String nombrePedido, List<Platillo> listaPlatillos) {
        platillos = new ArrayList<>();
        tiempoTotal = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH:mm:ss");
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        this.numeroOrden = formatter.format(fechaHoraActual);

        this.nombrePedido = nombrePedido;
        agregarListaPlatillos(listaPlatillos);
    }

    public void agregarPlatillo(Platillo platillo) {
        platillos.add(platillo);
        tiempoTotal += platillo.getTiempoPreparacion();
        tiempoRestante = tiempoTotal;
    }

    public void agregarListaPlatillos(List<Platillo> listaPlatillos) {
        platillos.addAll(listaPlatillos);

        for (Platillo platillo : listaPlatillos) {
            tiempoTotal += platillo.getTiempoPreparacion();
        }

        tiempoRestante = tiempoTotal;
    }

    public List<Platillo> getPlatillos() {
        return platillos;
    }

    public int getTiempoTotal() {
        return tiempoTotal;
    }
    public String getOrden() {
        return numeroOrden;
    }

    public String getNombrePedido() {
        return nombrePedido;
    }

    public void setTiempoRestante(int tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }

    public int getTiempoRestante() {
        return tiempoRestante;
    }
}
