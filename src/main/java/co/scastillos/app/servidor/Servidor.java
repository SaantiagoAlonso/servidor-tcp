package co.scastillos.app.servidor;

import co.scastillos.app.configuracion.ConfigTCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private ConfigTCP configTCP;
    private ServerSocket serverSocket;

    public Servidor(int puerto, String host) {
        this.configTCP = new ConfigTCP(puerto, host, 5000);
    }

    public void iniciar() throws IOException {
        configTCP.conectar();
        serverSocket = new ServerSocket(configTCP.getPuerto());
        System.out.println("Servidor iniciado en el puerto " + configTCP.getPuerto());

        while (true) {
            System.out.println("Esperando conexiones...");
            Socket socketCliente = serverSocket.accept(); // Acepta una nueva conexión
            System.out.println("Nuevo cliente conectado: " + socketCliente.getInetAddress());

            // Crear un nuevo hilo para manejar al cliente
            Thread hiloCliente = new Thread(new ManejadorCliente(socketCliente));
            hiloCliente.start();
        }
    }

    public void detener() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
        configTCP.desconectar();
        System.out.println("Servidor detenido.");
    }
}
