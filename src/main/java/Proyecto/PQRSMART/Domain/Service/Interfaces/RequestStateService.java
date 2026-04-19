package Proyecto.PQRSMART.Domain.Service.Interfaces;

import Proyecto.PQRSMART.Domain.Dto.RequestStateDTO;
import Proyecto.PQRSMART.Persistence.Entity.RequestState;

import java.util.List;
import java.util.Optional;

public interface RequestStateService {
    RequestStateDTO save(RequestStateDTO requestStateDTO);
    List<RequestStateDTO> getAll();
    Optional<RequestStateDTO> findById(Long id);
    Optional<RequestState> findByName(String name);

}
