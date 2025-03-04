package co.scastillos.app.configuracion;

public class ConfigTCP {
    private int puerto;
    private String host;
    private int timeout;

    public ConfigTCP(int puerto, String host, int timeout) {
        this.puerto = puerto;
        this.host = host;
        this.timeout = timeout;
    }

    public void conectar() {
        System.out.println("Conectando al servidor TCP en " + host + ":" + puerto);
    }

    public void desconectar() {
        System.out.println("Desconectando del servidor TCP");
    }

    public int getPuerto() {
        return puerto;
    }

    public String getHost() {
        return host;
    }
}