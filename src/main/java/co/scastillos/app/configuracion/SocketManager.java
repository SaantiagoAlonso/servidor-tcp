package co.scastillos.app.configuracion;


import java.io.*;
import java.net.Socket;

public class SocketManager {
//    private Socket socket;
//
//    public SocketManager(String host, int puerto) throws IOException {
//        this.socket = new Socket(host, puerto);
//    }
//
//    public void enviarDatos(String mensaje) throws IOException {
//        OutputStream output = socket.getOutputStream();
//        PrintWriter writer = new PrintWriter(output, true);
//        writer.println(mensaje);
//        System.out.println("Datos enviados: " + mensaje);
//    }
//
//    public String recibirDatos() throws IOException {
//        InputStream input = socket.getInputStream();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//        String mensaje = reader.readLine();
//        System.out.println("Datos recibidos: " + mensaje);
//        return mensaje;
//    }
//
//    public void cerrarConexion() throws IOException {
//        socket.close();
//        System.out.println("Conexión cerrada");
//    }

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    // Constructor para el cliente (usa host y puerto)
    public SocketManager(String host, int puerto) throws IOException {
        this(new Socket(host, puerto));
    }

    // Constructor para el servidor (recibe un Socket existente)
    public SocketManager(Socket socket) throws IOException {
        this.socket = socket;
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    // Enviar datos al otro extremo
    public void enviarDatos(String mensaje) {
        writer.println(mensaje);
        System.out.println("Datos enviados: " + mensaje);
    }

    // Recibir datos del otro extremo
    public String recibirDatos() throws IOException {
        String mensaje = reader.readLine();
        System.out.println("Datos recibidos: " + mensaje);
        return mensaje;
    }

    // Cerrar conexión
    public void cerrarConexion() throws IOException {
        socket.close();
        System.out.println("Conexión cerrada");
    }
}
