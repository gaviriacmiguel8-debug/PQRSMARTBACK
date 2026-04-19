package Proyecto.PQRSMART.Domain.Service.Interfaces;

import Proyecto.PQRSMART.Domain.Dto.IdentificationTypeDTO;

import java.util.List;
import java.util.Optional;

public interface IdentificationTypeService {
    IdentificationTypeDTO save(IdentificationTypeDTO identificationTypeDTO);
    List<IdentificationTypeDTO> getAll();
    Optional<IdentificationTypeDTO> findById(Long id);
}
