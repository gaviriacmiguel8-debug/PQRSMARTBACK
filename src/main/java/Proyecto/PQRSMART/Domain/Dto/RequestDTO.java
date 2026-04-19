package Proyecto.PQRSMART.Domain.Dto;


import lombok.*;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RequestDTO {
    private Long idRequest;
    private UsuarioDto user;
    private RequestTypeDTO requestType;
    private DependenceDTO dependence;
    private CategoryDTO category;
    private String description;
    private LocalDate date;
    private String answer;
    private RequestStateDTO requestState;
    private String mediumAnswer;
    private String archivo;
    private String archiveAnswer;
    private String radicado;

}
