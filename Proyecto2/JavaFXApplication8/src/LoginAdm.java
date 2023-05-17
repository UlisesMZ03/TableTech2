
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * Clase GameMenuDemo que implementa la aplicación principal del juego
 * BuscaminasFX.
 *
 * @author Ulises Mendez
 */
public class LoginAdm extends Application {

    private MasterApp buscaminas;
    private GameMenu gameMenu;

    private Stage primaryStage;
    private Image img = new Image("file:src/images/FondoLoginAdm.png");
    ImageView imgView = new ImageView(img);
  
    private File file = new File("/C:/Users/ulise/Desktop/TEC/Algoritmos y estructura de datos I/BuscaMinas/ventana busca minas/src/sounds/botonMenu.mp3");
    private Media media = new Media(file.toURI().toString());
    private MediaPlayer mediaPlayer;

    /**
     *
     * Método start que inicia la aplicación con la ventana principal.
     *
     * @param primaryStage el objeto Stage principal que se utilizará para
     * mostrar la aplicación.
     *
     * @throws Exception si ocurre algún error durante la ejecución del método.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        Pane root = new Pane();
        root.setPrefSize(400, 400);

        imgView.setFitWidth(400);
        imgView.setFitHeight(400);

        gameMenu = new GameMenu();
        gameMenu.setVisible(true);

        root.getChildren().addAll(imgView, gameMenu);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     *
     * La clase GameMenu representa un menú para un juego. Es una clase anidada
     * privada de otra clase.
     */
    private class GameMenu extends Parent {

        private static Label labelUsuario;
        private static Label labelPassword;
        private static TextField campoUsuario;
        private static PasswordField campoPassword;
        private static Label labelUsuarioReg;
        private static Label labelPasswordReg;
        private static TextField campoUsuarioReg;
        private static PasswordField campoPasswordReg;
        private final Label labelRespuesta;
        private final Label labelRespuesta2;
        private Button button1;

        private MasterApp clienteApp;
        private static final String HOST = "localhost";
        private static final int PUERTO = 8080;
        private String respuesta;

        /**
         *
         * Crea una nueva instancia de la clase GameMenu.
         */
        public GameMenu() {
            button1 = new Button("SING IN");
            button1.setFont(Font.font("Roboto", FontWeight.BOLD, 16));

           button1.setStyle("-fx-background-color: #333366; -fx-text-fill: white; -fx-border-style: none;");

            HBox hboxx = new HBox(button1);
            hboxx.setAlignment(Pos.CENTER);
            hboxx.setSpacing(0);
            hboxx.setTranslateX(90);
            hboxx.setTranslateY(75);
            VBox menu0 = new VBox(10);

            menu0.setTranslateX(60);
            menu0.setTranslateY(130);


            final int offset = 400;

            //menu1.setTranslateX(offset);

            labelUsuario = new Label("USERNAME");
            labelUsuario.setFont(Font.font("Roboto", FontWeight.BOLD, 10));
            labelUsuario.setTextFill(Color.web("#CCCCCC"));

            labelUsuario.setLayoutX(50);
            labelUsuario.setLayoutY(60);

            campoUsuario = new TextField();
            campoUsuario.setStyle("-fx-background-radius: 20; -fx-background-color: #5C5CA5;");
            campoUsuario.setPrefWidth(280);
            campoUsuario.setLayoutX(150);
            campoUsuario.setLayoutY(50);

            labelPassword = new Label("PASSWORD");
            labelPassword.setFont(Font.font("Roboto", FontWeight.BOLD, 10));
            labelPassword.setTextFill(Color.web("#CCCCCC"));
            labelPassword.setLayoutX(50);
            labelPassword.setLayoutY(100);

            campoPassword = new PasswordField();
            campoPassword.setStyle("-fx-background-radius: 20; -fx-background-color: #5C5CA5;");
            campoPassword.setLayoutX(150);
            campoPassword.setLayoutY(100);

            labelRespuesta = new Label();
            labelRespuesta.setLayoutX(150);
            labelRespuesta.setLayoutY(200);
            
            labelRespuesta2 = new Label();
            labelRespuesta2.setLayoutX(150);
            labelRespuesta2.setLayoutY(200);

            labelUsuarioReg = new Label("USERNAME");
            labelUsuarioReg.setFont(Font.font("Roboto", FontWeight.BOLD, 10));
            labelUsuarioReg.setTextFill(Color.web("#CCCCCC"));

            labelUsuarioReg.setLayoutX(50);
            labelUsuarioReg.setLayoutY(60);

            campoUsuarioReg = new TextField();
            campoUsuarioReg.setStyle("-fx-background-radius: 20; -fx-background-color: #5C5CA5;");
            campoUsuarioReg.setPrefWidth(280);
            campoUsuarioReg.setLayoutX(150);
            campoUsuarioReg.setLayoutY(50);

            labelPasswordReg = new Label("PASSWORD");
            labelPasswordReg.setFont(Font.font("Roboto", FontWeight.BOLD, 10));
            labelPasswordReg.setTextFill(Color.web("#CCCCCC"));
            labelPasswordReg.setLayoutX(50);
            labelPasswordReg.setLayoutY(100);

            campoPasswordReg = new PasswordField();
            campoPasswordReg.setStyle("-fx-background-radius: 20; -fx-background-color: #5C5CA5;");
            campoPasswordReg.setLayoutX(150);
            campoPasswordReg.setLayoutY(100);

            MenuButton btnLogin = new MenuButton("   SING IN", 90, 30, 10);

            

            btnLogin.setOnMouseClicked(event -> {
                if (campoPassword.getText().isEmpty()){
                campoPassword.setPromptText("Contraseña vacia");
            }
            if(campoUsuario.getText().isEmpty()){
                campoUsuario.setPromptText("Nombre de usuario vacio");
                
            }
            if (!campoPassword.getText().isEmpty()&&!campoUsuario.getText().isEmpty()){
        
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
                enviarCredenciales();
                //gameMenu.setVisible(false);
            }});


            HBox hbox = new HBox(btnLogin);
          
            menu0.getChildren().addAll(labelUsuario, campoUsuario, labelPassword, campoPassword, hbox, labelRespuesta);
          
            Rectangle bg = new Rectangle(800, 600);
            bg.setFill(Color.GREY);
            bg.setOpacity(0.01);

            getChildren().addAll(bg, menu0, hboxx);
        }

        private void enviarCredenciales() {
            try {

                // Conectar con el servidor
                Socket servidorSocket = new Socket(HOST, PUERTO);

                // Crear flujos de entrada/salida para comunicación con el servidor
                ObjectOutputStream salida = new ObjectOutputStream(servidorSocket.getOutputStream());
                ObjectInputStream entrada = new ObjectInputStream(servidorSocket.getInputStream());
                // Obtener credenciales del usuario
                String usuario = campoUsuario.getText();
                String password = campoPassword.getText();
                // Crear objeto Mensaje con la acción "register" y los datos de usuario y contraseña
                //Credenciales credenciales = new Credenciales(usuario, password);
                Mensaje mensaje = new Mensaje("loginAdm", usuario, password,"","");

                // Enviar objeto Mensaje al servidor
                salida.writeObject(mensaje);

                // Leer respuesta del servidor
                respuesta = (String) entrada.readObject();

                labelRespuesta.setText(respuesta);

                // Si la respuesta es "OK", abrir una nueva ventana
                if (respuesta.equals("OK")) {
                    if (clienteApp == null) {
                        clienteApp = new MasterApp();
                    }
                    clienteApp.start(new Stage());
                    primaryStage.close();

                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error de autenticación");
                    alert.setHeaderText("Usuario o contraseña inválidos");
                    alert.setContentText("Por favor, verifique que ha ingresado correctamente su usuario y contraseña e intente de nuevo.");

                    alert.showAndWait();

                }

                // Cerrar flujos de entrada/salida y conexión con el servidor
                entrada.close();
                salida.close();
                servidorSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        
    }

    /**
     *
     * La clase MenuButton representa un botón de menú. Es una clase anidada
     * privada y estática de otra clase.
     *
     * @author Ulises Mendez
     */
    private static class MenuButton extends StackPane {

        private Text text;

        /**
         *
         * Crea una nueva instancia de la clase MenuButton con un nombre, una
         * posición x y y y una traslación dada.
         *
         * @param name el nombre del botón
         *
         * @param x la posición x del botón
         *
         * @param y la posición y del botón
         *
         * @param translate la traslación del botón
         */
        public MenuButton(String name, int x, int y, int translate) {
            Font pixelFont = Font.font("Roboto", FontWeight.BOLD, 16);
            //Font pixelFont = Font.loadFont("file:/C:/Users/ulise/Desktop/TEC/Algoritmos y estructura de datos I/BuscaMinas/ventana busca minas/src/fonts/digital-7.ttf", 20);
            text = new Text(name);
            text.setFont(pixelFont);
            text.setFill(Color.WHITE);

            Rectangle bg = new Rectangle(x, y);
            bg.setArcWidth(10);
            bg.setArcHeight(10);
            bg.setOpacity(0.6);
            bg.setFill(Color.DARKGREY);
            bg.setEffect(new GaussianBlur(2));

            setAlignment(Pos.CENTER_LEFT);
            setRotate(-0.5);
            getChildren().addAll(bg, text);

            setOnMouseEntered(event -> {
                bg.setTranslateX(translate);
                text.setTranslateX(translate);
                bg.setFill(Color.GREY);
                text.setFill(Color.WHITE);
            });

            setOnMouseExited(event -> {
                bg.setTranslateX(0);
                text.setTranslateX(0);
                bg.setFill(Color.DARKGREY);
                text.setFill(Color.WHITE);
            });

            DropShadow drop = new DropShadow(50, Color.GREY);
            drop.setInput(new Glow());

            setOnMousePressed(event -> setEffect(drop));
            setOnMouseReleased(event -> setEffect(null));
        }
    }

    /**
     *
     * El método main es el punto de entrada de la aplicación. Se encarga de
     * iniciar la aplicación JavaFX.
     *
     * @param args los argumentos de línea de comando (no se utilizan en este
     * caso)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
