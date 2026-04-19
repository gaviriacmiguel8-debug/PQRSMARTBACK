package Proyecto.PQRSMART.Domain.Service.Interfaces;

import Proyecto.PQRSMART.Domain.Dto.DependenceDTO;

import java.util.List;
import java.util.Optional;

public interface DependenceService {
    DependenceDTO save(DependenceDTO dependenceDTO);
    DependenceDTO delete(DependenceDTO dependenceDTO);
    List<DependenceDTO> getAll();
    Optional<DependenceDTO> findById(Long id);
}
