import java.io.Serializable;

public class MensajePedido implements Serializable {
    private String accion; // "register" o "login"
    private Pedido pedido;

    public MensajePedido(String accion, Pedido pedido) {
        this.accion = accion;
        this.pedido = pedido;
    }

    public String getAccion() {
        return accion;
    }

    public Pedido getPedido() {
        return pedido;
    }
}
