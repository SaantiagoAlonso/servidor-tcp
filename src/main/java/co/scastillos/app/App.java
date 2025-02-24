package co.scastillos.app;

import co.scastillos.app.conexion_bd.RepoCuenta;
import co.scastillos.app.conexion_bd.RepoUsuario;
import co.scastillos.app.entidades.Cuenta;
import co.scastillos.app.entidades.Usuario;
import co.scastillos.app.servidor.Servidor;
import org.h2.tools.Server;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException, SQLException {
            System.out.println("Hello World!");

            RepoUsuario repoUsuario = new RepoUsuario();
            RepoCuenta repoCuenta = new RepoCuenta();

            // Crear usuario
            Usuario usuario = Usuario.builder()
                    .cedula(12345678)
                    .name("Juan Pérez")
                    .build();
            repoUsuario.guardar(usuario);

            // Crear cuenta
            Cuenta cuenta = Cuenta.builder()
                    .nCuenta(1001)
                    .saldo(5000.0)
                    .usuario(usuario)
                    .build();

            repoCuenta.guardar(cuenta);

            Server.main();
            System.out.println("✅ Datos insertados.");
            Servidor servidor = new Servidor(5000, "localhost");
            servidor.iniciar();


    }
}
