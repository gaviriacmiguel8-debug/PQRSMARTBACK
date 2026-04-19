package Proyecto.PQRSMART.Domain.Service.Interfaces;

import Proyecto.PQRSMART.Domain.Dto.CategoryDTO;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    CategoryDTO save(CategoryDTO categoryDTO);
    List<CategoryDTO> getAll();
    Optional<CategoryDTO> findById(Long id);
}
