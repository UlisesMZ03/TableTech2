
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class AppCliente extends Application {

    private VBox contenedorBotones;
    private static Label labelRespuesta;
    private VBox menu0;
    private VBox menu1;
    private static TextField campoUsuarioReg;
    private static PasswordField campoPasswordReg;
    private final String HOST = "localhost";
    private final int PUERTO = 8080;
    private String respuesta;
    private TextField campoUsuarioEditar;
    private TextField campoContraEditar;
    private TextField campoNewUsuarioEditar;
    private TextField campoNewContraEditar;
    TextField campoFiltro;
    private VBox menu2;
    private TextField campoNombrePlatillo;
    private TextField campoCalorias;
    private TextField campoTiempoPrep;
    private TextField campoPrecio;
    private VBox menu3;
    private TextField campoNombrePlatilloEditar;
   private Platillo PlatilloSeleccionado;

    private TextField campoNewPrecioPlatilloEditar;
    private TextField campoNewTiempoPlatilloEditar;
    private TextField campoNewCaloriasPlatilloEditar;
    private BufferedReader bufferedReader;
    private ObservableList<Platillo> items;
    private Gson gson;
    private VBox menu4;
    private Pedido colaPedidos;
    private Label labelPedidosCola;
    private String NombreUsuario;
    private boolean filtro;
    private ObservableList<Pedido> observablePedidos;
    private ObservableList<Usuario> listaUsuarios;

    public AppCliente(String nombreUsuario) {
        NombreUsuario = nombreUsuario;
    }

    @Override

    public void start(Stage primaryStage) throws FileNotFoundException, IOException {
        String estiloBotones = "-fx-background-color: transparent; -fx-text-fill: white;";
        // Crear los botones
       
        Button boton4 = new Button("Nuevo Pedido");
        Button boton5 = new Button("Historial de pedidos");
        // Crear el panel que cambiará según el botón presionado
        StackPane panelDerecho = new StackPane();
        panelDerecho.setStyle("-fx-background-color: #9BA4B5;"); // Establecer un color de fondo

        // Configurar los botones

        boton4.setPrefWidth(200);
        boton4.setAlignment(Pos.CENTER_LEFT);
        boton4.setStyle(estiloBotones);
        boton4.setCursor(Cursor.HAND);
        boton4.setOnMouseEntered(e -> boton4.setStyle("-fx-background-color: #f2f2f2; -fx-text-fill: black;"));
        boton4.setOnMouseExited(e -> boton4.setStyle(estiloBotones));
        boton5.setPrefWidth(200);
        boton5.setAlignment(Pos.CENTER_LEFT);
        boton5.setStyle(estiloBotones);
        boton5.setCursor(Cursor.HAND);
        boton5.setOnMouseEntered(e -> boton5.setStyle("-fx-background-color: #f2f2f2; -fx-text-fill: black;"));
        boton5.setOnMouseExited(e -> boton5.setStyle(estiloBotones));
        contenedorBotones = new VBox(20);
        // Crear el contenedor de los botones
        contenedorBotones.setPadding(new Insets(10));
        contenedorBotones.setStyle("-fx-background-color: #19376D;"); // Establecer un color de fondo
        contenedorBotones.setAlignment(Pos.TOP_LEFT);

        Button botonMostrarOcultar = new Button("☰");
        botonMostrarOcultar.setFont(Font.font("Roboto", 15));
        botonMostrarOcultar.setAlignment(Pos.CENTER_LEFT);
        botonMostrarOcultar.setStyle(estiloBotones);
        botonMostrarOcultar.setCursor(Cursor.HAND);
        botonMostrarOcultar.setOnMouseEntered(e -> botonMostrarOcultar.setStyle("-fx-background-color: #f2f2f2; -fx-text-fill: black;"));
        botonMostrarOcultar.setOnMouseExited(e -> botonMostrarOcultar.setStyle("-fx-background-color: #19376D; -fx-text-fill: white;"));
        // Crear un texto en el panel derecho
        Text texto = new Text("Panel derecho vacío");
        texto.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        panelDerecho.getChildren().add(texto);

        // Crear un contenedor para el panel derecho
        BorderPane contenedorDerecho = new BorderPane(panelDerecho);
        contenedorDerecho.setPadding(new Insets(1));

        // Crear un contenedor para todo
        BorderPane contenedorPrincipal = new BorderPane();
        contenedorPrincipal.setLeft(contenedorBotones);
        contenedorPrincipal.setCenter(contenedorDerecho);
        panelDerecho.getChildren().add(botonMostrarOcultar);

        botonMostrarOcultar.setOnAction(e -> {
            if (contenedorBotones.isVisible()) {
                // Si el contenedor de botones es visible, lo ocultamos y expandimos el contenedor derecho
                contenedorBotones.setVisible(false);
                contenedorDerecho.setPrefWidth(Double.MAX_VALUE);
                contenedorPrincipal.setLeft(null);
                contenedorDerecho.setAlignment(botonMostrarOcultar, Pos.CENTER_RIGHT); // Alineamos el botón a la derecha

                contenedorDerecho.getChildren().add(botonMostrarOcultar);

            } else {
                // Si el contenedor de botones es ocultado, lo mostramos y restauramos el tamaño del contenedor derecho
                contenedorBotones.setVisible(true);
                contenedorDerecho.setPrefWidth(Region.USE_COMPUTED_SIZE);
                contenedorPrincipal.setLeft(contenedorBotones);
                BorderPane.setAlignment(botonMostrarOcultar, Pos.CENTER_LEFT); // Alineamos el botón a la izquierda

                contenedorBotones.getChildren().add(0, botonMostrarOcultar);

            }

        });

        contenedorBotones.getChildren().addAll(botonMostrarOcultar, boton4, boton5);
        // Establecer la acción de los botones

        
        menu3 = new VBox(10);
        menu4 = new VBox(10);

       
        boton4.setOnAction(e
                -> {
            panelDerecho.getChildren().setAll(menu3);
            
            boton4.setStyle("-fx-background-color: gray; -fx-text-fill: white;");
            boton5.setStyle(estiloBotones);
        }
        );

        Label labelNuevoPedido = new Label("Nuevo Pedido");
        labelNuevoPedido.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
        labelNuevoPedido.setTextFill(Color.web("#19376D"));
        //labelNuevoPedido.setAlignment(Pos.CENTER);

        MenuButton btnAgregar = new MenuButton("Agregar", 75, 30, 5);
        MenuButton btnPedir = new MenuButton("Pedir", 75, 30, 5);

        btnAgregar.setPadding(new Insets(6));
        btnPedir.setPadding(new Insets(6));

        GridPane contenedorEditarPlat = new GridPane();

        contenedorEditarPlat.setHgap(10);

        gson = new Gson();
        bufferedReader = new BufferedReader(new FileReader("src/datos/platillos.json"));
        List<Platillo> comidas = Arrays.asList(gson.fromJson(bufferedReader, Platillo[].class));

        // Paso 3: Crear una ObservableList y agregar los objetos a la lista
        items = FXCollections.observableArrayList(comidas);
        ListView<Platillo> listViewPedido = new ListView<>();
        listViewPedido.setPrefSize(100, 100);
        // Paso 4: Crear una ListView y asignarle la ObservableList como su modelo de datos
        ListView<Platillo> listView2 = new ListView<>(items);
        listView2.setPrefSize(100, 100);
        // Paso 5: Definir una celda de fábrica para personalizar la apariencia de cada celda
        listView2.setCellFactory(param -> new ListCell<Platillo>() {
            @Override
            public void updateItem(Platillo comida, boolean empty) {
                super.updateItem(comida, empty);

                if (empty || comida == null) {
                    setText(null);
                } else {
                    setText(comida.getNombre() + " - " + comida.getCalorias() + " calorias - " + comida.getTiempoPreparacion() + " minutos - " + "₡" + comida.getPrecio());
                }
            }
        });
        // Creamos un ListView y le pasamos la lista observable de platillos

       
        // Fuera del evento del botón

        btnAgregar.setOnMouseClicked(event -> {
            System.out.println("Pedido: " + listViewPedido.getItems());
            Platillo platilloSeleccionado = listView2.getSelectionModel().getSelectedItem();
            if (platilloSeleccionado != null) {
                listViewPedido.getItems().add(platilloSeleccionado);
            }
        });

        btnPedir.setOnMouseClicked(event -> {
            items.clear();
            
            try {
                bufferedReader = new BufferedReader(new FileReader("src/datos/platillos.json"));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MasterApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            List<Platillo> nuevosPlatillos = Arrays.asList(gson.fromJson(bufferedReader, Platillo[].class));
            items.addAll(nuevosPlatillos);

            if (listViewPedido.getItems().isEmpty()) {
                System.out.println("Pedido vacio");

            } else {

                try {
                    enviarPedido(listViewPedido.getItems());
                    listViewPedido.getItems().clear();
                } catch (IOException ex) {
                    Logger.getLogger(MasterApp.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MasterApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Creamos un contenedor y añadimos el ListView
        VBox root = new VBox(labelNuevoPedido, listView2, btnAgregar, contenedorEditarPlat, listViewPedido, btnPedir);
        root.setAlignment(Pos.CENTER);
        menu3.getChildren().add(root);
        menu3.setAlignment(Pos.CENTER);
        menu3.setPadding(new Insets(30));

        boton5.setOnAction(e
                -> {
            panelDerecho.getChildren().setAll(menu4);
   
    
            boton4.setStyle(estiloBotones);

            boton5.setStyle("-fx-background-color: gray; -fx-text-fill: white;");
        }
        );
        Label labelColaPedidos = new Label("Cola de pedidos");
        labelColaPedidos.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
        labelColaPedidos.setTextFill(Color.web("#19376D"));
        labelColaPedidos.setAlignment(Pos.CENTER);
        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(0);
        ComboBox<String> filtroComboBox = new ComboBox<>();
        filtroComboBox.getItems().addAll("En curso", "Completados");
        filtroComboBox.setValue("En curso"); // Valor predeterminado

        filtroComboBox.setOnAction(e -> {
            String opcionSeleccionada = filtroComboBox.getValue();
            // Aplica el filtro según la opción seleccionada
            if (opcionSeleccionada.equals("En curso")) {
                filtro = false;
            } else if (opcionSeleccionada.equals("Completados")) {
                filtro = true;
            }
        });
        GridPane contenedorColaPedidos = new GridPane();
        contenedorColaPedidos.setAlignment(Pos.CENTER);
        contenedorColaPedidos.setPadding(new Insets(10));
        contenedorColaPedidos.setHgap(10);
        labelPedidosCola = new Label("Pedidos en cola");
        labelPedidosCola.setFont(Font.font("Roboto", FontWeight.BOLD, 12));
        labelPedidosCola.setTextFill(Color.web("#CCCCCC"));
        filtroComboBox.setTranslateX(384);
        contenedorColaPedidos.add(filtroComboBox, 1,0);


        ListView<Pedido> listViewPedi = new ListView<>();
        listViewPedi.setPrefWidth(500);
        progressBar.setPrefSize(400, 30);

        contenedorColaPedidos.add(listViewPedi, 1, 2);
        Thread colaThread = new Thread(() -> {
            while (true) {
                try {
                    Queuee colaaa = new Queuee();
                    Queuee colaAdm = new Queuee();
                    try {
                        // Cargar los datos desde el archivo JSON
                        colaAdm.fromJson("src/datos/pedidos.json");
                        colaaa.fromJson("src/datos/pedidos.json");
                    } catch (JsonSyntaxException e) {
                        // Manejar la excepción cuando el archivo JSON está vacío
                        System.err.println("El archivo JSON está vacío: " + e.getMessage());
                        return;
                    }
                    List<Pedido> listaPedidos = new ArrayList<>();
                    while (colaAdm.size() != 0) {
                        System.out.println("Pollllllsss " + colaAdm.peek());
                        Pedido pedido = (Pedido) colaAdm.peek();

                        listaPedidos.add(pedido);
                        colaAdm.remove();

                    }

                    for (Pedido pedido : listaPedidos) {

                        System.out.println(pedido.getNombrePedido());
                    }

                    Platform.runLater(() -> {
                        while (colaAdm.size() != 0) {
                            Pedido pedido = (Pedido) colaAdm.peek();
                            listaPedidos.add(pedido);
                            colaAdm.remove();
                        }
                        List<Pedido> listaPedidosFiltrada = listaPedidos.stream()
                                .filter(pedido -> pedido.getNombrePedido().equals(NombreUsuario))
                                .filter(pedido -> pedido.getTiempoRestante() > 0)
                                .collect(Collectors.toList());

                        List<Pedido> listaPedidosCompletadosFiltrada = readPedidosFromJSON("src/datos/pedidosCompletados.json").stream()
                                .filter(pedido -> pedido.getNombrePedido().equals(NombreUsuario))
                                .filter(pedido -> pedido.getTiempoRestante() == 0)
                                .collect(Collectors.toList());
                        if (filtro) {
                            observablePedidos = FXCollections.observableArrayList(listaPedidosCompletadosFiltrada);
                        } else {
                            observablePedidos = FXCollections.observableArrayList(listaPedidosFiltrada);
                        }

                        listViewPedi.setItems(observablePedidos);
                        listViewPedi.setCellFactory(param -> new ListCell<Pedido>() {
                            @Override
                            public void updateItem(Pedido comida, boolean empty) {
                                super.updateItem(comida, empty);

                                if (empty || comida == null) {
                                    setText(null);
                                } else {
                                    setText(comida.getNombrePedido() + "-" + comida.getOrden());
                                }
                            }
                        });
                    });

                    if (colaaa.size() != 0) {
                        colaPedidos = (Pedido) colaaa.peek();
                        if (colaPedidos.getNombrePedido().equals(NombreUsuario)) {
                            double progreso = (double) colaPedidos.getTiempoRestante() / colaPedidos.getTiempoTotal();
                            //listaPedidos.add(colaPedidos);
                            Platform.runLater(() -> progressBar.setProgress(progreso));
                        } else {
                            Platform.runLater(() -> progressBar.setProgress(-1));
                        }
                    } else {
                        Platform.runLater(() -> progressBar.setProgress(-1));
                    }

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // Handle InterruptedException
                } catch (NoSuchElementException e) {
                    Platform.runLater(() -> progressBar.setProgress(50));

                }
            }
        });

        menu4.setAlignment(Pos.CENTER);
        menu4.getChildren().addAll(labelColaPedidos, progressBar, contenedorColaPedidos);

        colaThread.setDaemon(true);
        colaThread.start();

        //colaThread.start();
        // Crear la escena
        Scene escena = new Scene(contenedorPrincipal, 800, 600);

        // Configurar la ventana
        primaryStage.setScene(escena);

        primaryStage.setTitle(
                "TableTech");
        primaryStage.show();

    }

    public List<Pedido> readPedidosFromJSON(String filename) {
        List<Pedido> pedidos = new ArrayList<>();

        try (FileReader reader = new FileReader(filename)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Pedido>>() {
            }.getType();
            pedidos = gson.fromJson(reader, listType);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo JSON: " + e.getMessage());
        }

        return pedidos;
    }

   



   

    private void enviarPedido(ObservableList<Platillo> items1) throws IOException, ClassNotFoundException {
        // Obtener credenciales del usuario

        // Enviar credenciales al servidor (código aquí)
        // Conectar con el servidor
        Socket servidorSocket = new Socket(HOST, PUERTO);

        // Crear flujos de entrada/salida para comunicación con el servidor
        ObjectOutputStream salida = new ObjectOutputStream(servidorSocket.getOutputStream());
        ObjectInputStream entrada = new ObjectInputStream(servidorSocket.getInputStream());
        // Obtener credenciales del usuario

        // Crear objeto Mensaje con la acción "register" y los datos de usuario y contraseña
        //Credenciales credenciales = new Credenciales(usuario, password);
        MensajePedido mensaje = new MensajePedido("agregarPedido", new Pedido(NombreUsuario, items1));

        // Enviar objeto Mensaje al servidor
        salida.writeObject(mensaje);

        // Leer respuesta del servidor
        String respuesta = (String) entrada.readObject();


    }

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
            Color color = Color.web("#19376D");
            bg.setFill(color);

            bg.setEffect(new GaussianBlur(2));

            setAlignment(Pos.CENTER);
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
                bg.setFill(color);
                text.setFill(Color.WHITE);
            });

            DropShadow drop = new DropShadow(50, Color.GREY);
            drop.setInput(new Glow());

            setOnMousePressed(event -> setEffect(drop));
            setOnMouseReleased(event -> setEffect(null));
        }
    }

  

}
