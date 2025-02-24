package co.scastillos.app.servicio;

import co.scastillos.app.conexion_bd.RepoCuenta;
import co.scastillos.app.dto.RecibirDatosDto;
import co.scastillos.app.dto.RespuestaSaldoDto;
import co.scastillos.app.entidades.Cuenta;

import java.util.Optional;

public class ServicioCuenta {

    private RepoCuenta repoCuenta;

    public ServicioCuenta() {
        this.repoCuenta = new RepoCuenta();
    }

    public Double consultarSaldoPorCedula(Integer cedula) {
        return repoCuenta.obtenerSaldoPorCedulaUsuario(cedula);
    }

    public Double consultarSaldoPorNumeroCuenta(Integer numeroCuenta) {
        return repoCuenta.obtenerSaldoPorNumeroCuenta(numeroCuenta);
    }

    public RespuestaSaldoDto consultarCuenta(RecibirDatosDto datosConsulta) {
        if(datosConsulta.getTipoBusqueda().equals("cedula")){
//            Optional<Cuenta>  cuenta = repoCuenta.buscarPorNCuenta(datosConsulta.getValor());
            Cuenta cuenta = repoCuenta.buscarPorCedula(datosConsulta.getValor()).orElse(null);
            if(cuenta == null){
                return new RespuestaSaldoDto(null,"cedula no encontrada");
            }
            return new RespuestaSaldoDto(cuenta.getSaldo(),"busqueda por numero de cedula");
        }else {
            if (datosConsulta.getTipoBusqueda().equals("cuenta")){
//               Optional<Cuenta>  cuenta = repoCuenta.buscarPorNCuenta(datosConsulta.getValor());
                Cuenta cuenta = repoCuenta.buscarPorNCuenta(datosConsulta.getValor()).orElse(null);
                if(cuenta == null){
                    return new RespuestaSaldoDto(null,"numero de cuenta no encontrado");
                }
                return new RespuestaSaldoDto(cuenta.getSaldo(),"busqueda por numero de cuenta");
            }
        }
        return new RespuestaSaldoDto(null,"peticion no valida");
    }

//    CREAR METODO PARA SONSIGNAR A OTRA CUENTA POR NUMERO DE CUENTA
//    ************************************************************
//    SE DEBE VALIDAR QUE HAYA EL SALDO SUCFICIENTE PARA PODER CONSIGNAR
//    SE DEBE VERIFICAR QUE LA CUENTA A LA QUE SE VA REALIZAR LA CONSIGNACION EXISTA
//    SE DEBE DESCONTAR EL VALOR DEL SALDO ACTUAL DE LA CUENTA REMITENTE
//    SE DEBE AUMENTAR EL SALDO DE LA CUENTA DESTINO
//    SE DEBE PERSISTIR BD
//    AL FINALIZAR LA TRANSACION SE DEBE GUARDAR EL MOVIMIENTO TANTO DEL REMITENTE COMO EL DESTINATARIO
//    SE DEBE PERSISTIR LA INFOMACION

//    public void realizarTransaccion(){
//        Double saldoRemitente = repoCuenta.obtenerSaldoPorNumeroCuenta(2345);
//        if(saldoRemitente > saldoTransferir){
//
//        }
//    }

//    public void RealizarTransaccion(){
//        Cuenta cuenta = repoCuenta.buscarPorNCuenta(2345)
//                .orElseThrow(() -> new RuntimeException("mensaje"));
//
//        if(cuenta.getSaldo() > ;
//    }


}


