package Proyecto.PQRSMART.Domain.Service;


import Proyecto.PQRSMART.Domain.Dto.UsuarioDto;
import Proyecto.PQRSMART.Domain.Mapper.UsuarioMapper;
import Proyecto.PQRSMART.Domain.Service.Interfaces.PdfServices;
import Proyecto.PQRSMART.Domain.Service.Interfaces.UsuarioService;
import Proyecto.PQRSMART.Persistence.Entity.Request;
import Proyecto.PQRSMART.Persistence.Entity.StateUser;
import Proyecto.PQRSMART.Persistence.Entity.User;
import Proyecto.PQRSMART.Persistence.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PdfServices pdfServices;

    private final PasswordEncoder passwordEncoder;


    public List<UsuarioDto> getAll() {
        return usuarioRepository.findAll().stream().map(UsuarioMapper::toDto).collect(Collectors.toList());
    }

    public Optional<UsuarioDto> findById(Long id) {
        return usuarioRepository.findById(id).map(UsuarioMapper::toDto);
    }

    public Optional<User> findByIds(Long id) {
        return usuarioRepository.findById(id);
    }

    public UsuarioDto save(UsuarioDto usuarioDto) {
        usuarioRepository.save(UsuarioMapper.toEntity(usuarioDto));
        return usuarioDto;
    }

    public User saves(User usuario) {
        usuarioRepository.save(usuario);
        return usuario;
    }

    public UsuarioDto upda(UsuarioDto userDTO) {
        Optional<User> existingUserOptional = usuarioRepository.findById(userDTO.getId());
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setStateUser(userDTO.getStateUser());
            usuarioRepository.save(existingUser);
            return userDTO;
        } else {
            usuarioRepository.save(UsuarioMapper.toEntity(userDTO));
            return userDTO;
        }
    }


    public void verifyUser(String username) {
        User user = usuarioRepository.findByUser(username);
        if (user != null){
            user.setStateUser(new StateUser(2L, "ACTIVO"));
            usuarioRepository.save(user);
        }
    }

    public User findByEmail(String email) {
        return usuarioRepository.findByEmail(email) ;
    }

    public void resetPassword(String email, String newPassword) {
        User user = usuarioRepository.findByEmail(email);
        if (user != null){
            user.setPassword(passwordEncoder.encode(newPassword));
            usuarioRepository.save(user);
        }
        else{
            System.out.println("Email no encontrado");
        }
    }

    public ResponseEntity<UsuarioDto> getProfile() {

        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User user = usuarioRepository.findByUser(userDetails.getUsername());

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UsuarioDto dto = UsuarioDto.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .user(user.getUser())
                .identificationNumber(user.getIdentificationNumber())
                .stateUser(user.getStateUser())
                .role(user.getRole())
                .number(user.getNumber())
                .build();

        return ResponseEntity.ok(dto);
    }

    public ResponseEntity<String> getInitial() {

        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User user = usuarioRepository.findByUser(userDetails.getUsername());

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        String name = user.getName();
        String initial = name != null && !name.isEmpty()
                ? String.valueOf(name.charAt(0)).toUpperCase()
                : "";

        return ResponseEntity.ok(initial);
    }

    public byte[] generateReport() {
        try {
            List<User> usuarios = usuarioRepository.findAll();
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Usuarios");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Nombre");
            header.createCell(2).setCellValue("Apellido");
            header.createCell(3).setCellValue("Usuario");
            header.createCell(4).setCellValue("Correo");
            header.createCell(5).setCellValue("Rol");
            header.createCell(6).setCellValue("Numero Identificación");
            header.createCell(7).setCellValue("Numero Teléfono");
            header.createCell(8).setCellValue("Tipo Identificación");
            header.createCell(9).setCellValue("Tipo Persona");
            header.createCell(10).setCellValue("Dependencia");
            header.createCell(11).setCellValue("Estado");

            int rowNum = 1;

            for (User u : usuarios) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(u.getId());
                row.createCell(1).setCellValue(u.getName());
                row.createCell(2).setCellValue(u.getLastName());
                row.createCell(3).setCellValue(u.getUser());
                row.createCell(4).setCellValue(u.getEmail());
                row.createCell(5).setCellValue(u.getRole().name());

                row.createCell(6).setCellValue(
                        u.getIdentificationNumber() != null ? u.getIdentificationNumber().toString() : ""
                );

                row.createCell(7).setCellValue(
                        u.getNumber() != null ? u.getNumber().toString() : ""
                );

                row.createCell(8).setCellValue(
                        u.getIdentificationType() != null ? u.getIdentificationType().getNameIdentificationType() : ""
                );

                row.createCell(9).setCellValue(
                        u.getPersonType() != null ? u.getPersonType().getNamePersonType() : ""
                );

                row.createCell(10).setCellValue(
                        u.getDependence() != null ? u.getDependence().getNameDependence() : ""
                );

                row.createCell(11).setCellValue(
                        u.getStateUser() != null ? u.getStateUser().getState() : ""
                );
            }

            // Ajustar tamaño de columnas automáticamente
            for (int i = 0; i <= 11; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            return outputStream.toByteArray();
        }
        catch (Exception e){
            throw new RuntimeException("Error generando el reporte de usuarios", e);
        }
    }
}
