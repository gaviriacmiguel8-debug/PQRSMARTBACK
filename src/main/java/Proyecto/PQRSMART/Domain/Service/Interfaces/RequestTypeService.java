package Proyecto.PQRSMART.Domain.Service.Interfaces;

import Proyecto.PQRSMART.Domain.Dto.RequestTypeDTO;

import java.util.List;
import java.util.Optional;

public interface RequestTypeService {
    RequestTypeDTO save(RequestTypeDTO requestTypeDTO);
    List<RequestTypeDTO> getAll();
    Optional<RequestTypeDTO> findById(Long id);
}
