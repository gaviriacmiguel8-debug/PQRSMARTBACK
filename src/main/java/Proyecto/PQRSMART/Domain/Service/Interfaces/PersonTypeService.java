package Proyecto.PQRSMART.Domain.Service.Interfaces;

import Proyecto.PQRSMART.Domain.Dto.PersonTypeDTO;

import java.util.List;
import java.util.Optional;

public interface PersonTypeService {
    PersonTypeDTO save(PersonTypeDTO personTypeDTO);
    List<PersonTypeDTO> getAll();
    Optional<PersonTypeDTO> findById(Long id);
}
