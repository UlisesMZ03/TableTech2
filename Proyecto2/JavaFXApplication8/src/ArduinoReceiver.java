
import com.fazecast.jSerialComm.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * La clase ArduinoReceiver se utiliza para recibir y procesar datos enviados
 * por el puerto serial de Arduino. También proporciona métodos para configurar
 * y enviar señales a Arduino.
 */
public class ArduinoReceiver {

    private SimpleIntegerProperty variableY = new SimpleIntegerProperty();
    private SimpleIntegerProperty variableX = new SimpleIntegerProperty();
    private SimpleIntegerProperty variableSelec = new SimpleIntegerProperty();
    SerialPort port;

    /**
     * El constructor de la clase ArduinoReceiver configura y abre el puerto
     * serie para la comunicación con Arduino. También inicia un hilo para leer
     * los datos enviados por Arduino y actualizar las propiedades
     * correspondientes.
     */
    public ArduinoReceiver() {
        // Configurar el puerto serie
        port = SerialPort.getCommPort("COM12"); // Cambiar a tu puerto Arduino
        port.openPort();
        port.setBaudRate(9600); // Configurar la velocidad del puerto serie según la configuración de Arduino
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 100, 0); // Configurar los tiempos de espera

        // Crear un hilo para leer los datos del puerto serie y actualizar la propiedad variableProperty
        Thread thread = new Thread(() -> {
            while (true) {
                byte[] buffer = new byte[1024];
                int numRead = port.readBytes(buffer, buffer.length);

                if (numRead > 0) {
                    String receivedData = new String(buffer, 0, numRead);
                    Matcher matcher = Pattern.compile("\\d+").matcher(receivedData);

                    while (matcher.find()) {
                        int data = Integer.parseInt(matcher.group());
                        if (data == 3) {
                            if (variableY.get() < 7) {
                                variableY.set(variableY.get() + 1);
                            }

                        } else if (data == 1) {
                            if (variableY.get() > 0) {
                                variableY.set(variableY.get() - 1);
                            }
                        } else if (data == 2) {
                            if (variableX.get() < 7) {
                                variableX.set(variableX.get() + 1);
                            }
                        } else if (data == 4) {
                            if (variableX.get() > 0) {
                                variableX.set(variableX.get() - 1);
                            }
                        } else if (data == 5) {
                            variableSelec.set(1);
                            System.out.println("Se presiono para colocar mina: " + variableSelec);
                        } else if (data == 6) {
                            variableSelec.set(2);
                            System.out.println("Se presiono para colocar mina: " + variableSelec);
                        }

                    }

                }
            }
        }
        );
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * El método closePort se utiliza para cerrar el puerto serie y terminar la
     * comunicación con Arduino.
     */
    public void closePort() {
        port.closePort();
    }

    /**
     * El método setVariableY se utiliza para establecer el valor de la
     * propiedad variableY.
     *
     * @param value el nuevo valor de la propiedad variableY.
     */
    public void setVariableY(int value) {
        variableY.set(value);
    }

    /**
     * El método setVariableX se utiliza para establecer el valor de la
     * propiedad variableX.
     *
     * @param value el nuevo valor de la propiedad variableX.
     */
    public void setVariableX(int value) {
        variableX.set(value);
    }

    /**
     * El método setVariableSelec se utiliza para establecer el valor de la
     * propiedad variableSelec.
     *
     * @param value el nuevo valor de la propiedad variableSelec.
     */
    public void setVariableSelec(int value) {
        variableSelec.set(value);
    }

    /*
    
     */
    /**
     *
     * Devuelve la propiedad variableY.
     *
     * @return la propiedad variableY.
     */
    public SimpleIntegerProperty variableProperty() {
        return variableY;
    }

    /**
     *
     * Devuelve la propiedad variableX.
     *
     * @return la propiedad variableX.
     */
    public SimpleIntegerProperty variable2XProperty() {
        return variableX;
    }

    /**
     *
     * Devuelve la propiedad variableSelec.
     *
     * @return la propiedad variableSelec.
     */
    public SimpleIntegerProperty variableSProperty() {
        return variableSelec;
    }

    /**
     *
     * Envia una señal al puerto serie.
     *
     * @param senal la señal a enviar.
     */
    public void enviarSenal(String senal) {
        port.writeBytes(senal.getBytes(), senal.length());
        System.out.println("Señal enviada");
    }

}
