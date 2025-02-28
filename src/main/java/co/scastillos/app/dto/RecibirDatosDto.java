package co.scastillos.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecibirDatosDto {

    private String tipoBusqueda;
    private Integer valor;

}
