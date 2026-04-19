package Proyecto.PQRSMART.Persistence.Repository;

import Proyecto.PQRSMART.Persistence.Entity.Dependence;
import Proyecto.PQRSMART.Persistence.Entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByUserUser(String user);
    List<Request> findByDependence(Dependence dependence);
    List<Request> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
