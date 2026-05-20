package Proyecto.PQRSMART.Domain.Dto;


import Proyecto.PQRSMART.Persistence.Entity.Priority;
import Proyecto.PQRSMART.Persistence.Entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private Priority priority;
    private UsuarioDto assignedUser;
    private String rejectReason;
    private Boolean aiCoherence;
    private Double relevance;

}
