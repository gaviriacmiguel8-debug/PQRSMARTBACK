package Proyecto.PQRSMART.Domain.Service.Interfaces;

import Proyecto.PQRSMART.Domain.Dto.UsuarioDto;
import Proyecto.PQRSMART.Persistence.Entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<UsuarioDto> getAll();
    Optional<UsuarioDto> findById(Long id);
    Optional<User> findByIds(Long id);
    UsuarioDto save(UsuarioDto usuarioDto);
    User saves(User usuario);
    UsuarioDto upda(UsuarioDto userDTO);
    void verifyUser(String username);
    User findByEmail(String email);
    void resetPassword(String email, String newPassword);
    ResponseEntity<UsuarioDto>  getProfile();
    ResponseEntity<String>  getInitial();
    byte[] generateReport();
}
