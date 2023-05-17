
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class AVLTree {

    private String ruta = "src/datos/platillos.json";
    private Node root;

    Node getRoot() {
        return root;
    }

    public class Node {

        public Platillo data;
        public Node left;
        public Node right;
        public int height;
        public int size;

        public Node(Platillo data) {
            this.data = data;
            this.height = 1;
            this.size = 1;
        }
    }

    public AVLTree() {
        this.root = null;
        cargarDesdeJSON(ruta);
    }

    public void editar(String nombre, int calorias, int tiempoPreparacion, double precio) {
        Node node = root;

        while (node != null) {
            int cmp = nombre.compareTo(node.data.getNombre());

            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                // Se encontró el platillo, se actualizan sus propiedades
                node.data.setCalorias(calorias);
                node.data.setTiempoPreparacion(tiempoPreparacion);
                node.data.setPrecio(precio);
                System.out.println("Se ha editado el platillo con éxito");
                guardar(ruta);
                return;
            }
        }

        // Si no se encontró el platillo
        System.out.println("No se encontró el platillo");
    }

    public boolean contains(String nombre) {
        Node node = root;

        while (node != null) {
            int cmp = nombre.compareTo(node.data.getNombre());

            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                System.out.println("Si lo contiene");
                return true;

            }
        }
        System.out.println("No lo contiene");
        return false;
    }

    public void cargarDesdeJSON(String nombreArchivo) {
        try {
            // Leemos el archivo JSON y lo convertimos a una lista de objetos Platillo
            Gson gson = new Gson();
            FileReader fileReader = new FileReader(nombreArchivo);
            List<Platillo> listaPlatillos = gson.fromJson(fileReader, new TypeToken<List<Platillo>>() {
            }.getType());

            // Insertamos los objetos en el árbol AVL
            for (Platillo platillo : listaPlatillos) {
                insert(platillo);
            }

            fileReader.close();
        } catch (IOException e) {
            System.out.println("Error al leer el archivo JSON: " + e.getMessage());
        }
    }

    // Método para guardar el árbol en un archivo JSON
    public void guardar(String filename) {
        List<Platillo> listaPlatillos = new ArrayList<>();
        crearLista(root, listaPlatillos);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(listaPlatillos);

        try (Writer writer = new FileWriter(filename)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar para crear una lista de objetos Platillo a partir del árbol
    private void crearLista(Node node, List<Platillo> listaPlatillos) {
        if (node != null) {
            crearLista(node.left, listaPlatillos);
            listaPlatillos.add(node.data);
            crearLista(node.right, listaPlatillos);
        }
    }

    public void imprimir() {
        imprimir(root);
    }

    private void imprimir(Node node) {
        if (node != null) {
            imprimir(node.left);
            System.out.println(node.data.toString());
            imprimir(node.right);
        }
    }

    public void eliminar(String nombre) {
        root = eliminar(root, nombre);
    }

    private Node eliminar(Node node, String nombre) {
        // Si el nodo es null, no se encontró el platillo y no se puede eliminar
        if (node == null) {
            System.out.println("No se encontró el platillo");
            return null;
        }

        int cmp = nombre.compareTo(node.data.getNombre());

        if (cmp < 0) {
            node.left = eliminar(node.left, nombre);
        } else if (cmp > 0) {
            node.right = eliminar(node.right, nombre);
        } else {
            // Se encontró el nodo con el platillo a eliminar

            // Si el nodo tiene dos hijos, se encuentra el sucesor inorden, se copia su valor al nodo actual y se elimina el sucesor inorden
            if (node.left != null && node.right != null) {
                Node sucesor = sucesorInorden(node.right);
                node.data = sucesor.data;
                node.right = eliminar(node.right, sucesor.data.getNombre());
            } else {
                // Si el nodo tiene uno o cero hijos, se puede eliminar directamente y el hijo (si lo tiene) se convierte en el nuevo nodo
                Node hijo = (node.left != null) ? node.left : node.right;
                node = hijo;
                System.out.println("Se ha eliminado el platillo con éxito");
                guardar(ruta);
            }
        }

        // Si el nodo era una hoja, ya fue eliminado y se retorna null
        if (node == null) {
            return null;
        }

        // Se actualizan las propiedades del nodo
        node.height = 1 + Math.max(height(node.left), height(node.right));
        node.size = 1 + size(node.left) + size(node.right);

        int balance = balance(node);

        if (balance > 1 && balance(node.left) >= 0) {
            return rotateRight(node);
        }

        if (balance > 1 && balance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1 && balance(node.right) <= 0) {
            return rotateLeft(node);
        }

        if (balance < -1 && balance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

// Método auxiliar que retorna el sucesor inorden de un nodo
    private Node sucesorInorden(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public void insert(Platillo data) {
        root = insert(root, data);
    }

    private Node insert(Node node, Platillo data) {
        if (node == null) {
            return new Node(data);
        }

        int cmp = data.compareTo(node.data);
        if (cmp < 0) {
            node.left = insert(node.left, data);
        } else if (cmp > 0) {
            node.right = insert(node.right, data);
        } else {
            // Duplicate key not allowed
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        node.size = 1 + size(node.left) + size(node.right);

        int balance = balance(node);

        if (balance > 1 && data.compareTo(node.left.data) < 0) {
            return rotateRight(node);
        }

        if (balance < -1 && data.compareTo(node.right.data) > 0) {
            return rotateLeft(node);
        }

        if (balance > 1 && data.compareTo(node.left.data) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1 && data.compareTo(node.right.data) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private Node rotateLeft(Node node) {
        Node x = node.right;
        Node T2 = x.left;

        x.left = node;
        node.right = T2;

        node.height = 1 + Math.max(height(node.left), height(node.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));

        node.size = 1 + size(node.left) + size(node.right);
        x.size = 1 + size(x.left) + size(x.right);

        return x;
    }

    private Node rotateRight(Node node) {
        Node x = node.left;
        Node T2 = x.right;

        x.right = node;
        node.left = T2;

        node.height = 1 + Math.max(height(node.left), height(node.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));

        node.size = 1 + size(node.left) + size(node.right);
        x.size = 1 + size(x.left) + size(x.right);

        return x;
    }

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private int size(Node node) {
        return node == null ? 0 : node.size;
    }

    private int balance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }
}
