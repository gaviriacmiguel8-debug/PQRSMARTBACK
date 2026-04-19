package Proyecto.PQRSMART.Domain.Dto;




import Proyecto.PQRSMART.Persistence.Entity.Dependence;
import Proyecto.PQRSMART.Persistence.Entity.State;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CategoryDTO {
    private Long idCategory;
    private String nameCategory;
    private Dependence dependence;
    private State state;

}
