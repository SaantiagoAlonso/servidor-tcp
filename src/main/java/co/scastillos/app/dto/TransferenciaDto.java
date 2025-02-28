package co.scastillos.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferenciaDto {

    private Integer nCuentaRemitente;
    private Integer nCuentaDestino;
    private Double valor;

}
