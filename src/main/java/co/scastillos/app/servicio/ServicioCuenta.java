package co.scastillos.app.servicio;

import co.scastillos.app.conexion_bd.RepoCuenta;
import co.scastillos.app.conexion_bd.RepoMovimiento;
import co.scastillos.app.dto.*;
import co.scastillos.app.entidades.Cuenta;
import co.scastillos.app.entidades.Movimiento;
import jakarta.transaction.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ServicioCuenta {

    private RepoCuenta repoCuenta;
    private RepoMovimiento repoMovimiento;

    public ServicioCuenta() {
        this.repoCuenta = new RepoCuenta();
        this.repoMovimiento = new RepoMovimiento();
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

    @Transactional
    public String realizarTransaccion(TransferenciaDto trasferencia){
        Cuenta cuenta = repoCuenta.buscarPorNCuenta(trasferencia.getNCuentaRemitente()).orElse(null);

        if(cuenta.getSaldo() > trasferencia.getValor()){
            Cuenta cuentaDestino = repoCuenta.buscarPorNCuenta(trasferencia.getNCuentaDestino()).orElse(null);
            cuenta.setSaldo(cuenta.getSaldo() - trasferencia.getValor());
            cuentaDestino.setSaldo(cuentaDestino.getSaldo() + trasferencia.getValor());
//          MOVIMIENTO DE QUIEN REALIZA LA TRANSACCION
            Movimiento movimientoRemitente = Movimiento.builder()
                    .fecha(new Date())
                    .cuentaOrigen(cuenta.getNCuenta())
                    .cuentaDestino(cuentaDestino.getNCuenta())
                    .accion("transferencia")
                    .cuenta(cuenta)
                    .nCuenta(cuenta.getNCuenta())
                    .valor(trasferencia.getValor())
                    .build();
            repoMovimiento.guardar(movimientoRemitente);
//            movimineto de quien recibe la transaccion
            Movimiento movimientoDestinatario = Movimiento.builder()
                    .fecha(new Date())
                    .cuentaOrigen(cuenta.getNCuenta())
                    .cuentaDestino(null)
                    .accion("transferencia")
                    .cuenta(cuentaDestino)
                    .nCuenta(cuentaDestino.getNCuenta())
                    .valor(trasferencia.getValor())
                    .build();
            repoMovimiento.guardar(movimientoDestinatario);
            cuenta.getMovimientos().add(movimientoRemitente);
            cuentaDestino.getMovimientos().add(movimientoRemitente);
            repoCuenta.guardar(cuenta);
            repoCuenta.guardar(cuentaDestino);

            return "transferencia realizada";
        }
        return "no se pudo completar la transferencia";
    }


    public ConsultaMovDto consultarMovimientos(ConsultaMovDto consutaMovDto){
        List<MovimientoDto> movimientos = repoMovimiento.listarMovimientosPorCuenta(consutaMovDto.getNCuenta());
        System.out.println(movimientos.toString());
        return new ConsultaMovDto(consutaMovDto.getNCuenta(),movimientos,"consulta Exitosa");
    }

}


