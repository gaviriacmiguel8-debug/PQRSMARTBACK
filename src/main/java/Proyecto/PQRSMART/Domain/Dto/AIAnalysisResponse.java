package Proyecto.PQRSMART.Domain.Dto;

import Proyecto.PQRSMART.Persistence.Entity.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AIAnalysisResponse {

    private Boolean valid;

    private Boolean coherence;

    private Double relevance;

    private String reason;

    private Priority priority;

    private Long assignedUserId;
}
