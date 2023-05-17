
import java.io.Serializable;

public class Mensaje implements Serializable {
    private String accion; // "register" o "login"
    private String usuario;
    private String password;
    private String usuarioNew;
    private String passwordNew;

    public Mensaje(String accion, String usuario, String password, String usuarioNew, String passwordNew) {
        this.accion = accion;
        this.usuario = usuario;
        this.password = password;
        this.usuarioNew = usuarioNew;
        this.passwordNew = passwordNew;
    }

    public String getAccion() {
        return accion;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getPassword() {
        return password;
    }
    public String getUsuarioNew() {
        return usuarioNew;
    }
    public String getPasswordNew() {
        return passwordNew;
    }
}
