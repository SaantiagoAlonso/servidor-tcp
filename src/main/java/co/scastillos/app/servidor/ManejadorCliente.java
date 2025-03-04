package co.scastillos.app.servidor;

import co.scastillos.app.configuracion.SocketManager;
import co.scastillos.app.dto.*;
import co.scastillos.app.servicio.ServicioCuenta;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.Socket;

public class ManejadorCliente implements Runnable {

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

                RespuestaDto respuesta = controladorSolicitudes(mensaje);

                String respuestaJson = objectMapper.writeValueAsString(respuesta);
                socketManager.enviarDatos(respuestaJson);
            }

        } catch (IOException e) {
            System.out.println("Error al manejar al cliente: " + e.getMessage());
        } finally {
//            cerrarConexion();
        }
    }

//    NUEVO
    private RespuestaDto controladorSolicitudes (String mensaje) throws JsonProcessingException {
        SolicitudDto datos = objectMapper.readValue(mensaje, SolicitudDto.class);

        switch (datos.getAccion()){
            case "consultar":
                String jsonContenido = objectMapper.writeValueAsString(datos.getContenido());
                RecibirDatosDto datosConsulta = objectMapper.readValue(jsonContenido, RecibirDatosDto.class);
                RespuestaSaldoDto respuesta = servicioCuenta.consultarCuenta(datosConsulta);
                return new RespuestaDto("ok", respuesta);
            case "transferencia":
                String contenido = objectMapper.writeValueAsString(datos.getContenido());
                TransferenciaDto transferenciaDto = objectMapper.readValue(contenido,TransferenciaDto.class);
                String res =  servicioCuenta.realizarTransaccion(transferenciaDto);
                return new RespuestaDto("ok",res);
            case "movimientos":
                String contenid = objectMapper.writeValueAsString(datos.getContenido());
                ConsultaMovDto consMov = objectMapper.readValue(contenid,ConsultaMovDto.class);
                ConsultaMovDto consulta = servicioCuenta.consultarMovimientos(consMov);
                return new RespuestaDto("ok",consulta);

            default:
                return new RespuestaDto("error", "Acción no reconocida");
        }

    }

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
