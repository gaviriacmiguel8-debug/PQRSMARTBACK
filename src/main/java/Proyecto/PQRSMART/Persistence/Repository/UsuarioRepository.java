package Proyecto.PQRSMART.Persistence.Repository;

import Proyecto.PQRSMART.Domain.Dto.UsuarioDto;
import Proyecto.PQRSMART.Persistence.Entity.Dependence;
import Proyecto.PQRSMART.Persistence.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUser(String user);
    List<User> findByDependence(Dependence dependence);

    User findByUser(String username);
    User findByEmail(String email);

    boolean existsByUser(String user);
    boolean existsByEmail(String email);
    boolean existsByIdentificationNumber(BigInteger identificationNumber);
    boolean existsByNumber(BigInteger number);

}
