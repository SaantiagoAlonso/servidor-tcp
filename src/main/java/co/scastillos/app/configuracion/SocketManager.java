package co.scastillos.app.configuracion;


import java.io.*;
import java.net.Socket;

public class SocketManager {

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;


    public SocketManager(String host, int puerto) throws IOException {
        this(new Socket(host, puerto));
    }

    public SocketManager(Socket socket) throws IOException {
        this.socket = socket;
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void enviarDatos(String mensaje) {
        writer.println(mensaje);
        System.out.println("Datos enviados: " + mensaje);
    }
    public String recibirDatos() throws IOException {
        String mensaje = reader.readLine();
        System.out.println("Datos recibidos: " + mensaje);
        return mensaje;
    }

    public void cerrarConexion() throws IOException {
        socket.close();
        System.out.println("Conexión cerrada");
    }
}
