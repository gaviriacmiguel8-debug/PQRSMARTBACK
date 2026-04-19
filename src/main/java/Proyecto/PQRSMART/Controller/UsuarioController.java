package Proyecto.PQRSMART.Controller;

import Proyecto.PQRSMART.Domain.Dto.UsuarioDto;
import Proyecto.PQRSMART.Domain.Service.Interfaces.UsuarioService;
import Proyecto.PQRSMART.Domain.Service.UsuarioServiceImpl;
import Proyecto.PQRSMART.Persistence.Entity.Request;
import Proyecto.PQRSMART.Persistence.Entity.StateUser;
import Proyecto.PQRSMART.Persistence.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Usuario/")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    /*@PostMapping("/save")
    public UsuarioDto save(@RequestBody UsuarioDto usuarioDto){
        return usuarioService.save(usuarioDto);
    }

    @PostMapping("/saves")
    public User saves(@RequestBody User usuario){
        return usuarioService.saves(usuario);
    }
*/
    @GetMapping("/get")
    public List<UsuarioDto> get(){return usuarioService.getAll();}

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody UsuarioDto usuarioDto) {
        Optional<UsuarioDto> personTypeDTOOptional = usuarioService.findById(usuarioDto.getId());
        if(personTypeDTOOptional.isPresent()) {
            usuarioService.save(usuarioDto);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UsuarioDto usuarioDto) {
        Optional<UsuarioDto> userDTOOptional = usuarioService.findById(id);
        if (userDTOOptional.isPresent()) {
            UsuarioDto existingUsuario = userDTOOptional.get();
            existingUsuario.setStateUser(usuarioDto.getStateUser());
            // Actualizar otros campos si es necesario
            UsuarioDto updatedRequestDTO = usuarioService.save(existingUsuario); // Guardar los cambios en la solicitud existente
            return ResponseEntity.ok(updatedRequestDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<User> deactivarUsuario(@PathVariable Long id) {
        Optional<User> userOptional = usuarioService.findByIds(id);
        if (userOptional.isPresent()) {
            User usuario = userOptional.get();
            // Asignar el estado "CANCELADA" de la entidad RequestState
            usuario.setStateUser(new StateUser(3L, "DESACTIVADO"));
            usuarioService.saves(usuario);
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }
    @PatchMapping("/activate/{id}")
    public ResponseEntity<User> activarUsuario(@PathVariable Long id) {
        Optional<User> userOptional = usuarioService.findByIds(id);
        if (userOptional.isPresent()) {
            User usuario = userOptional.get();
            // Asignar el estado "CANCELADA" de la entidad RequestState
            usuario.setStateUser(new StateUser(2L, "ACTIVADO"));
            usuarioService.saves(usuario);
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<UsuarioDto> getProfile(){

        return usuarioService.getProfile();

    }
    @GetMapping("/initial")
    public ResponseEntity<String> getInitial(){

        return usuarioService.getInitial();

    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> generateReport() throws Exception {

        byte[] excel = usuarioService.generateReport();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename("reporte-usuario.xlsx")
                        .build()
        );

        return new ResponseEntity<>(excel, headers, HttpStatus.OK);
    }
}
