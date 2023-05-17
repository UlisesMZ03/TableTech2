
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javafx.scene.control.TreeItem;
import org.w3c.dom.NodeList;

public class BinarySearchTreeAdm {

    private Node root;

    public BinarySearchTreeAdm() {
        root = null;
        try {
            File file = new File("src/datos/administradores.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            NodeList nodeList = document.getElementsByTagName("administrador");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String nombreUsuario = element.getAttribute("nombreUsuario");
                String contrasena = element.getAttribute("contrasena");
                Usuario usuario = new Usuario(nombreUsuario, contrasena);
                insert(usuario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(Usuario usuario) {
        root = insertRecursive(root, usuario);
        toXml();
    }

    private Node insertRecursive(Node current, Usuario usuario) {
        if (current == null) {
            return new Node(usuario);
        }

        if (usuario.getNombreUsuario().compareTo(current.usuario.getNombreUsuario()) < 0) {
            current.left = insertRecursive(current.left, usuario);
        } else if (usuario.getNombreUsuario().compareTo(current.usuario.getNombreUsuario()) > 0) {
            current.right = insertRecursive(current.right, usuario);
        } else {
            // el usuario ya existe, no se agrega nada y se muestra un mensaje
            System.out.println("El usuario " + usuario.getNombreUsuario() + " ya existe.");
            return current;
        }

        return current;
    }

    public boolean contains(String nombreUsuario) {
        return containsRecursive(root, nombreUsuario);
    }

    private boolean containsRecursive(Node current, String nombreUsuario) {
        if (current == null) {
            return false;
        }

        if (nombreUsuario.equals(current.usuario.getNombreUsuario())) {
            return true;
        }

        if (nombreUsuario.compareTo(current.usuario.getNombreUsuario()) < 0) {
            return containsRecursive(current.left, nombreUsuario);
        } else {
            return containsRecursive(current.right, nombreUsuario);
        }
    }

    public void printInOrder() {
        printInOrderRecursive(root);
    }

    private void printInOrderRecursive(Node current) {
        if (current == null) {
            return;
        }

        printInOrderRecursive(current.left);
        System.out.println(current.usuario);
        printInOrderRecursive(current.right);
    }

    public Usuario get(String nombreUsuario) {
        Node current = root;
        while (current != null) {
            if (nombreUsuario.equals(current.usuario.getNombreUsuario())) {
                return current.usuario;
            } else if (nombreUsuario.compareTo(current.usuario.getNombreUsuario()) < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }

    public void toXml() {
        try {
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(new File("src/datos/administradores.xml")));
            writer.writeStartDocument();
            writer.writeStartElement("Administradores");

            toXmlRecursive(root, writer);

            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void toXmlRecursive(Node current, XMLStreamWriter writer) throws XMLStreamException {
        if (current == null) {
            return;
        }

        writer.writeStartElement("administrador");
        writer.writeAttribute("nombreUsuario", current.usuario.getNombreUsuario());
        writer.writeAttribute("contrasena", current.usuario.getPassword());
        writer.writeEndElement();

        toXmlRecursive(current.left, writer);
        toXmlRecursive(current.right, writer);
    }

    public TreeItem<Usuario> toTreeItem() {
        return toTreeItemRecursive(root);
    }

    private TreeItem<Usuario> toTreeItemRecursive(Node current) {
        if (current == null) {
            return null;
        }

        TreeItem<Usuario> item = new TreeItem<>(current.usuario);
        item.setExpanded(true);
        item.getChildren().add(toTreeItemRecursive(current.left));
        item.getChildren().add(toTreeItemRecursive(current.right));

        return item;
    }

    public void edit(String oldName, String newName, String password) {
        System.out.println(oldName);
        System.out.println(newName);
        
        // Verificar si el nuevo nombre ya existe en el árbol
        if (newName==oldName) {
            System.out.println("Contraseña actualizada");
            root = deleteRecursive(root, oldName);
            Usuario newUsuario = new Usuario(newName, password);
            root = insertRecursive(root, newUsuario);
            toXml();
            return;
        }
        
        if (newName.equals(oldName)) {
            System.out.println("Contraseña actualizada");
            root = deleteRecursive(root, oldName);
            Usuario newUsuario = new Usuario(newName, password);
            root = insertRecursive(root, newUsuario);
            toXml();
            return;
        }
        
        if (contains(newName)) {
            System.out.println("El nuevo nombre de usuario ya existe.");
            return;
        }

        // Buscar el usuario a editar
        BinarySearchTreeAdm.Node node = search(root, oldName);
        if (node == null) {
            System.out.println("El usuario a editar no existe.");
            return;
        }

        // Crear el usuario con el nuevo nombre
        Usuario newUsuario = new Usuario(newName, password);

        // Eliminar el usuario con el nombre viejo
        root = deleteRecursive(root, oldName);

        // Insertar el usuario con el nuevo nombre
        root = insertRecursive(root, newUsuario);

        // Guardar los cambios en el archivo XML
        toXml();
    }

    public void delete(String nombreUsuario) {
        // Buscar el usuario a eliminar
        BinarySearchTreeAdm.Node node = search(root, nombreUsuario);
        if (node == null) {
            System.out.println("El usuario a eliminar no existe.");
            return;
        }

        // Eliminar el usuario
        root = deleteRecursive(root, nombreUsuario);

        // Guardar los cambios en el archivo XML
        toXml();
    }

    private BinarySearchTreeAdm.Node deleteRecursive(BinarySearchTreeAdm.Node current, String nombreUsuario) {
        if (current == null) {
            return null;
        }

        if (nombreUsuario.equals(current.usuario.getNombreUsuario())) {
            if (current.left == null && current.right == null) {
                return null;
            }
            if (current.right == null) {
                return current.left;
            }
            if (current.left == null) {
                return current.right;
            }
            Usuario smallestValue = findSmallestValue(current.right);
            current.usuario = smallestValue;
            current.right = deleteRecursive(current.right, smallestValue.getNombreUsuario());
            return current;
        }
        if (nombreUsuario.compareTo(current.usuario.getNombreUsuario()) < 0) {
            current.left = deleteRecursive(current.left, nombreUsuario);
            return current;
        }
        current.right = deleteRecursive(current.right, nombreUsuario);
        return current;
    }

    private Usuario findSmallestValue(BinarySearchTreeAdm.Node root) {
        return root.left == null ? root.usuario : findSmallestValue(root.left);
    }

    private BinarySearchTreeAdm.Node search(BinarySearchTreeAdm.Node node, String nombreUsuario) {
        if (node == null || nombreUsuario.equals(node.usuario.getNombreUsuario())) {
            return node;
        }
        if (nombreUsuario.compareTo(node.usuario.getNombreUsuario()) < 0) {
            return search(node.left, nombreUsuario);
        }
        return search(node.right, nombreUsuario);
    }

    private class Node {

        private Usuario usuario;
        private Node left;
        private Node right;

        public Node(Usuario usuario) {
            this.usuario = usuario;
            left = null;
            right = null;
        }
    }

}
