
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLStreamException;

public class Servidor {

    public static AVLTree arbol = new AVLTree();
    private static final int PUERTO = 8080;
    public static Queuee cola = new Queuee();
    public static Pedido head;
    static List<Pedido> listaPedidosCompletados = new ArrayList<>();

    public static void main(String[] args) throws XMLStreamException, FileNotFoundException, IOException, ClassNotFoundException {
        vaciarJSON();
        if (readPedidosFromJSON("src/datos/pedidosCompletados.json") != null) {
            listaPedidosCompletados = readPedidosFromJSON("src/datos/pedidosCompletados.json");
        }

        // Crear un árbol binario de búsqueda para usuarios
        BinarySearchTreeUsr usuarios = new BinarySearchTreeUsr();
        BinarySearchTreeAdm administradores = new BinarySearchTreeAdm();

        // Creamos un hilo que disminuirá el tiempo de preparación del primer platillo en la cola
        Thread timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    try {
                        head = (Pedido) cola.peek();
                        if (head != null) {
                            System.out.println("Tiempo restante del primer pedido en la cola: " + head.getTiempoRestante() + " minutos");
                            if (head.getTiempoRestante() == 0) {
                                listaPedidosCompletados.add(head);
                                guardarPedidosEnJSON(listaPedidosCompletados, "src/datos/pedidosCompletados.json");
                                cola.remove();
                                System.out.println("Pedidos Completados: " + listaPedidosCompletados);
                                cola.toJson("src/datos/pedidos.json");
                                System.out.println("El platillo " + head.getNombrePedido() + " está listo para servir");
                            } else {
                                head.setTiempoRestante(head.getTiempoRestante() - 1);
                                cola.toJson("src/datos/pedidos.json");
                            }
                        }
                    } catch (NoSuchElementException e) {
// La cola está vacía

                    }
                    // Actualizar la cola en el servidor
                    synchronized (cola) {
                        cola.notifyAll();
                    }

                    try {
                        Thread.sleep(1000); // Esperamos un segundo antes de volver a revisar el tiempo
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        timerThread.start(); // Iniciamos el hilo

        try {
            // Crear un servidor socket
            ServerSocket servidorSocket = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado");

            while (true) {
                // Esperar a que un cliente se conecte
                Socket clienteSocket = servidorSocket.accept();
                // System.out.println("Cliente conectado: " + clienteSocket.getInetAddress());

                // Crear flujos de entrada/salida para comunicación con el cliente
                ObjectOutputStream salida = new ObjectOutputStream(clienteSocket.getOutputStream());
                ObjectInputStream entrada = new ObjectInputStream(clienteSocket.getInputStream());

                // Leer objeto Mensaje del cliente]
                //Mensaje mensaje = (Mensaje) entrada.readObject();
                // Leer objeto de entrada del cliente
                Object entradaObj = entrada.readObject();

                // Verificar si el objeto es del tipo Mensaje
                if (entradaObj instanceof Mensaje) {
                    Mensaje mensaje = (Mensaje) entradaObj;
                    // Verificar acción del mensaje
                    if (mensaje.getAccion().equals("login")) {
                        // Verificar credenciales con el árbol de usuarios válidos
                        boolean credencialesCorrectas = usuarios.contains(mensaje.getUsuario())
                                && usuarios.get(mensaje.getUsuario()).getPassword().equals(mensaje.getPassword());

                        // Enviar respuesta al cliente
                        if (credencialesCorrectas) {
                            salida.writeObject("OK");
                        } else {
                            salida.writeObject("ERROR");
                        }
                    } else if (mensaje.getAccion().equals("loginAdm")) {
                        // Verificar credenciales con el árbol de usuarios válidos
                        boolean credencialesCorrectas = administradores.contains(mensaje.getUsuario())
                                && administradores.get(mensaje.getUsuario()).getPassword().equals(mensaje.getPassword());

                        // Enviar respuesta al cliente
                        if (credencialesCorrectas) {
                            salida.writeObject("OK");
                        } else {
                            salida.writeObject("ERROR");
                        }
                    } else if (mensaje.getAccion().equals("register")) {

                        // Enviar respuesta al cliente
                        if (usuarios.contains(mensaje.getUsuario())) {
                            salida.writeObject("Nombre de usuario no disponible");
                        } else {
                            usuarios.insert(new Usuario(mensaje.getUsuario(), mensaje.getPassword()));
                            salida.writeObject("Se ha registrado con exito");
                        }
                    } else if (mensaje.getAccion().equals("registerAdm")) {

                        // Enviar respuesta al cliente
                        if (administradores.contains(mensaje.getUsuario())) {
                            salida.writeObject("Usuario no disponible");
                        } else {
                            administradores.insert(new Usuario(mensaje.getUsuario(), mensaje.getPassword()));
                            salida.writeObject("Usuario registrado con exito");
                        }
                    } else if (mensaje.getAccion().equals("deleteAdm")) {

                        // Enviar respuesta al cliente
                        if (administradores.contains(mensaje.getUsuario())) {
                            administradores.delete(mensaje.getUsuario());
                            administradores.toXml();
                            salida.writeObject("Usuario eliminado");
                        } else {

                            salida.writeObject("Usuario no encontrado");
                        }
                    } else if (mensaje.getAccion().equals("editAdm")) {

                        // Enviar respuesta al cliente
                        if (administradores.contains(mensaje.getUsuario())) {
                            if (mensaje.getUsuario().equals(mensaje.getUsuarioNew())) {
                                System.out.println("son iguales");
                            }
                            if (mensaje.getPasswordNew() == "") {
//                            System.out.println(mensaje.getUsuario());
//                            System.out.println(mensaje.getUsuarioNew());
//                            System.out.println(mensaje.getPasswordNew());
                                administradores.edit(mensaje.getUsuario(), mensaje.getUsuarioNew(), mensaje.getPassword());
                                administradores.toXml();
                                salida.writeObject("Usuario Editado");

                            } else {
                                administradores.edit(mensaje.getUsuario().toString(), mensaje.getUsuarioNew().toString(), mensaje.getPasswordNew());
                                administradores.toXml();
                                salida.writeObject("Usuario Editado");
                            }

                        } else {

                            salida.writeObject("Usuario no encontrado");
                        }
                    }
                    // El objeto es del tipo Mensaje
                } // Verificar si el objeto es del tipo Mensaje2
                if (entradaObj instanceof MensajePlatillo) {
                    MensajePlatillo mensaje = (MensajePlatillo) entradaObj;
                    if (mensaje.getAccion().equals("agregarPlatillo")) {

                        // Enviar respuesta al cliente
                        if (arbol.contains(mensaje.getNombrePlatillo())) {
                            salida.writeObject("Ya existe este platillo");
                        } else {

                            arbol.insert(new Platillo(mensaje.getNombrePlatillo(), mensaje.getCalorias(), mensaje.getTiempoPrep(), mensaje.getPrecio()));
                            salida.writeObject("Platillo agregado con exito");
                            arbol.guardar("src/datos/platillos.json");
                        }
                    } else if (mensaje.getAccion().equals("editPlat")) {

                        // Enviar respuesta al cliente
                        if (arbol.contains(mensaje.getNombrePlatillo())) {
                            arbol.editar(mensaje.getNombrePlatillo(), mensaje.getCalorias(), mensaje.getTiempoPrep(), mensaje.getPrecio());
                            salida.writeObject("Platillo editado");
                            arbol.guardar("src/datos/platillos.json");
                        } else {

                            salida.writeObject("Platillo no existe");
                            arbol.guardar("src/datos/platillos.json");
                        }
                    } else if (mensaje.getAccion().equals("elimPlat")) {

                        // Enviar respuesta al cliente
                        if (arbol.contains(mensaje.getNombrePlatillo())) {
                            arbol.eliminar(mensaje.getNombrePlatillo());
                            salida.writeObject("Platillo eliminado");
                            arbol.guardar("src/datos/platillos.json");
                        } else {

                            salida.writeObject("Platillo no existe");
                            arbol.guardar("src/datos/platillos.json");
                        }
                    }

                    // El objeto es del tipo Mensaje2
                }

                if (entradaObj instanceof MensajePedido) {

                    MensajePedido mensaje = (MensajePedido) entradaObj;
                    cola.add(mensaje.getPedido());

                    // Enviar respuesta al cliente
                    salida.writeObject("Pedido recibido con éxito");
                }

                // Cerrar flujos de entrada/salida y conexión con el cliente
                entrada.close();
                salida.close();
                clienteSocket.close();
                //System.out.println("Socket Cerrado");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    public static List<Pedido> readPedidosFromJSON(String filename) {
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

    private static void vaciarJSON() {
        try {
            // Crear un objeto Gson
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // Crear un objeto vacío
            Object objetoVacio = new Object(); // Reemplaza "Object" con el tipo de objeto que corresponda a tu JSON

            // Convertir el objeto vacío a JSON
            String jsonVacio = gson.toJson(objetoVacio);

            // Escribir el JSON vacío al archivo
            FileWriter writer = new FileWriter("src/datos/pedidos.json");
            writer.write(jsonVacio);
            writer.close();

            System.out.println("El JSON se ha vaciado exitosamente.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void guardarPedidosEnJSON(List<Pedido> listaPedidos, String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(listaPedidos, writer);
            System.out.println("Archivo '" + filename + "' guardado con éxito.");
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo JSON: " + e.getMessage());
        }
    }

    public static synchronized Queuee getCola() {
        return cola;
    }

}
