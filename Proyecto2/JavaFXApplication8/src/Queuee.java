
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Queuee<T> implements Serializable {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public Queuee() {
        head = null;
        tail = null;
        size = 0;
    }

    public void toJson(String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo JSON: " + e.getMessage());
        }
    }

    public void fromJson(String filename) {
        Gson gson = new GsonBuilder().create();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            Type queueType = new TypeToken<Queuee<Pedido>>() {
            }.getType();
            Queuee<Pedido> queue = gson.fromJson(reader, queueType);
           
            while (!queue.isEmpty()) {
                add((T) queue.remove());
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo JSON: " + e.getMessage());
        }
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            public boolean hasNext() {
                return current != null;
            }

            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                T item = current.item;
                current = current.next;
                return item;
            }
        };
    }

   public void add(T item) {
    Node<T> newNode = new Node<>(item);
    if (isEmpty()) {
        head = newNode;
        tail = newNode;
    } else {
        if (tail.item instanceof Pedido && ((Pedido) tail.item).getNombrePedido().equals("Carlos")) {
            Node<T> temp = head;
            while (temp.next != tail) {
                temp = temp.next;
            }
            temp.next = newNode;
            newNode.next = tail;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
    }
    size++;
}


    public T remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("La cola está vacía");
        }

        T item = head.item;
        head = head.next;
        size--;

        if (isEmpty()) {
            tail = null;
        }

        return item;
    }

    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("La cola está vacía");
        }

        return head.item;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private static class Node<T> implements Serializable {

        private T item;
        private Node<T> next;

        public Node(T item) {
            this.item = item;
            this.next = null;
        }
    }
}
