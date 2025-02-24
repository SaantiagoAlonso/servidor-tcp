package co.scastillos.app.servidor;

import co.scastillos.app.configuracion.SocketManager;
import co.scastillos.app.dto.RecibirDatosDto;
import co.scastillos.app.dto.RespuestaDto;
import co.scastillos.app.dto.RespuestaSaldoDto;
import co.scastillos.app.dto.SolicitudDto;
import co.scastillos.app.servicio.ServicioCuenta;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ManejadorCliente implements Runnable {

//    private final Socket socketCliente;
//    private final ServicioCuenta servicioCuenta;
//    private final ObjectMapper objectMapper;
//
//    public ManejadorCliente(Socket socketCliente) {
//        this.socketCliente = socketCliente;
//        this.servicioCuenta = new ServicioCuenta();
//        this.objectMapper = new ObjectMapper(); // Para procesar JSON
//    }
//
//    @Override
//    public void run() {
//        try {
//            SocketManager socketManager = new SocketManager(socketCliente);
//            System.out.println("Cliente conectado: " + socketCliente.getInetAddress());
//
//            String mensaje;
//            while ((mensaje = socketManager.recibirDatos()) != null) {
//                System.out.println("Mensaje recibido del cliente: " + mensaje);
//
//                // Procesar JSON y generar respuesta
//                RecibirDatosDto datos = objectMapper.readValue(mensaje, RecibirDatosDto.class);
//                RespuestaSaldoDto respuesta = procesarSolicitud(datos);
//
//                // Enviar la respuesta al cliente
//                socketManager.enviarDatos(respuesta);
//            }
//
//        } catch (IOException e) {
//            System.out.println("Error al manejar al cliente: " + e.getMessage());
//        } finally {
//            try {
//                socketCliente.close();
//                System.out.println("Conexión cerrada con el cliente.");
//            } catch (IOException e) {
//                System.out.println("Error al cerrar la conexión: " + e.getMessage());
//            }
//        }
//    }
//
//    // Método para procesar el JSON y consultar el saldo
//    private String procesarSolicitud(RecibirDatosDto datos) {
//        try {
//            // Deserializar JSON a objeto DTO
////            RecibirDatosDto datos = objectMapper.readValue(mensaje, RecibirDatosDto.class);
//
//            if ("cedula".equalsIgnoreCase(datos.getTipoBusqueda())) {
//                Double saldo = servicioCuenta.consultarSaldoPorCedula(datos.getValor());
//                return saldo != null ? "Saldo por cédula: " + saldo : "Cédula no encontrada.";
//            } else if ("cuenta".equalsIgnoreCase(datos.getTipoBusqueda())) {
//                Double saldo = servicioCuenta.consultarSaldoPorNumeroCuenta(datos.getValor());
//                return saldo != null ? "Saldo por cuenta: " + saldo : "Cuenta no encontrada.";
//            }
//            return "Error: tipoCuenta debe ser 'cedula' o 'cuenta'.";
//
//        } catch (Exception e) {
//            return "Error al procesar el JSON: " + e.getMessage();
//        }
//    }

    private final Socket socketCliente;
    private final ServicioCuenta servicioCuenta;
    private final ObjectMapper objectMapper;

    public ManejadorCliente(Socket socketCliente) {
        this.socketCliente = socketCliente;
        this.servicioCuenta = new ServicioCuenta();
        this.objectMapper = new ObjectMapper(); // Para procesar JSON
    }

    @Override
    public void run() {
        try  {
            SocketManager socketManager = new SocketManager(socketCliente);
            System.out.println("Cliente conectado: " + socketCliente.getInetAddress());

            String mensaje;
            while ((mensaje = socketManager.recibirDatos()) != null) {
                System.out.println("Mensaje recibido del cliente: " + mensaje);

                // Procesar el mensaje y generar respuesta
//                RespuestaSaldoDto respuesta = procesarSolicitud(mensaje);
                RespuestaDto respuesta = controladorSolicitudes(mensaje);

                // Convertir respuesta a JSON y enviarla al cliente
                String respuestaJson = objectMapper.writeValueAsString(respuesta);
                socketManager.enviarDatos(respuestaJson);
            }

        } catch (IOException e) {
            System.out.println("Error al manejar al cliente: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }

    // Método para procesar el JSON y consultar el saldo
    private RespuestaSaldoDto procesarSolicitud(String mensaje) {
        try {
            // Deserializar JSON a objeto DTO
            RecibirDatosDto datos = objectMapper.readValue(mensaje, RecibirDatosDto.class);

            if ("cedula".equalsIgnoreCase(datos.getTipoBusqueda())) {
                Double saldo = servicioCuenta.consultarSaldoPorCedula(datos.getValor());
                return (saldo != null) ? new RespuestaSaldoDto(saldo, "Saldo por cédula")
                        : new RespuestaSaldoDto(null, "Cédula no encontrada");

            } else if ("cuenta".equalsIgnoreCase(datos.getTipoBusqueda())) {
                Double saldo = servicioCuenta.consultarSaldoPorNumeroCuenta(datos.getValor());
                return (saldo != null) ? new RespuestaSaldoDto(saldo, "Saldo por cuenta")
                        : new RespuestaSaldoDto(null, "Cuenta no encontrada");
            }

            return new RespuestaSaldoDto(null, "Error: tipoBusqueda debe ser 'cedula' o 'cuenta'");

        } catch (Exception e) {
            return new RespuestaSaldoDto(null, "Error al procesar el JSON: " + e.getMessage());
        }
    }

//    NUEVO
    private RespuestaDto controladorSolicitudes (String mensaje) throws JsonProcessingException {
        SolicitudDto datos = objectMapper.readValue(mensaje, SolicitudDto.class);

        switch (datos.getAccion()){
            case "consultar":
//                RecibirDatosDto datosConsulta = objectMapper.readValue(datos.getContenido(),RecibirDatosDto.class);
//                RespuestaSaldoDto respuesta = servicioCuenta.consultarCuenta(datosConsulta);
//                String contenido = objectMapper.writeValueAsString(respuesta);
//                return new RespuestaDto("ok",contenido);
                // Asegurarse de que contenido sea una cadena JSON
                String jsonContenido = objectMapper.writeValueAsString(datos.getContenido());

                // Leer el valor JSON en un objeto RecibirDatosDto
                RecibirDatosDto datosConsulta = objectMapper.readValue(jsonContenido, RecibirDatosDto.class);
                RespuestaSaldoDto respuesta = servicioCuenta.consultarCuenta(datosConsulta);
//                String contenido = objectMapper.writeValueAsString(respuesta);
                return new RespuestaDto("ok", respuesta);
//            case "transferir":
//                servicioCuenta.transferir(datos.getContenido());
//                break;
//
//            case "retirar":
//                servicioCuenta.retirar(datos.getContenido());
//            case  ""
            default:
                return new RespuestaDto("error", "Acción no reconocida");
        }

    }

    // Método para cerrar la conexión con el cliente
    private void cerrarConexion() {
        try {
            if (!socketCliente.isClosed()) {
                socketCliente.close();
                System.out.println("Conexión cerrada con el cliente.");
            }
        } catch (IOException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
