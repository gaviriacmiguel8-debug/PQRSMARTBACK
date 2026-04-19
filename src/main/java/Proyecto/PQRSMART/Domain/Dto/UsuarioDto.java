package Proyecto.PQRSMART.Domain.Dto;

import Proyecto.PQRSMART.Persistence.Entity.Dependence;
import Proyecto.PQRSMART.Persistence.Entity.Role;
import Proyecto.PQRSMART.Persistence.Entity.StateUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDto {
    private  long id;

    private String name;

    private String email;

    private String user;

    private String lastName;

    private BigInteger identificationNumber;

    private BigInteger number;

    private StateUser stateUser;

    private DependenceDTO dependence;

    private Role role;
}
