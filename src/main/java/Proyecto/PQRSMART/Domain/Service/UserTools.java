package Proyecto.PQRSMART.Domain.Service;


import Proyecto.PQRSMART.Domain.Service.Interfaces.IEmailService;
import Proyecto.PQRSMART.Persistence.Entity.*;
import Proyecto.PQRSMART.Persistence.Repository.DependenceRepository;
import Proyecto.PQRSMART.Persistence.Repository.IdentificationTypeRepository;
import Proyecto.PQRSMART.Persistence.Repository.PersonTypeRepository;
import Proyecto.PQRSMART.Persistence.Repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class UserTools {
    private final UsuarioRepository userRepository;
    private final DependenceRepository dependenceRepository;
    private final IdentificationTypeRepository identificationTypeRepository;
    private final PersonTypeRepository personTypeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final IEmailService emailService;


    // ── LISTAR ────────────────────────────────────────────────────────────────
    @Tool(description = "Lista todos los usuarios registrados en el sistema con su id, nombre, apellido, email y rol")
    public String listarUsuarios() {
        List<User> lista = userRepository.findAll();
        if (lista.isEmpty()) return "No hay usuarios registrados.";

        StringBuilder sb = new StringBuilder("Usuarios registrados:\n");
        lista.forEach(u -> sb.append(String.format(
                "- [%d] %s %s | email: %s | rol: %s | estado: %s\n",
                u.getId(),
                u.getName(),
                u.getLastName(),
                u.getEmail(),
                u.getRole() != null ? u.getRole().name() : "sin rol",
                u.getStateUser() != null ? u.getStateUser().toString() : "sin estado"
        )));
        return sb.toString();
    }

    // ── BUSCAR POR EMAIL ──────────────────────────────────────────────────────
    @Tool(description = "Busca un usuario por su correo electrónico y retorna sus datos")
    public String buscarUsuarioPorEmail(
            @ToolParam(description = "Correo electrónico del usuario a buscar") String email
    ) {
        User u = userRepository.findByEmail(email);
        if (u == null) return "No encontrado";


        return String.format(
                "Usuario encontrado:\n- ID: %d\n- Nombre: %s %s\n- Email: %s\n- Rol: %s\n- Número: %s",
                u.getId(), u.getName(), u.getLastName(), u.getEmail(),
                u.getRole() != null ? u.getRole().name() : "sin rol",
                u.getNumber() != null ? u.getNumber().toString() : "sin número"
        );
    }

    // ── ACTUALIZAR ROL ────────────────────────────────────────────────────────
    @Tool(description = "Cambia el rol de un usuario. Los roles válidos son los definidos en el enum Role (ej: ADMIN, USER, etc.)")
    public String actualizarRol(
            @ToolParam(description = "Correo electrónico del usuario a modificar") String email,
            @ToolParam(description = "Nuevo rol a asignar (nombre exacto del enum Role)") String nuevoRol
    ) {
        User u = userRepository.findByEmail(email);
        if (u == null) return "No encontrado";
        try {
            Role rol = Role.valueOf(nuevoRol.toUpperCase());

            u.setRole(rol);
            userRepository.save(u);
            return "✅ Rol del usuario " + email + " actualizado a: " + rol.name();
        } catch (IllegalArgumentException e) {
            return "❌ Rol inválido: '" + nuevoRol + "'. Verifica los roles disponibles en el enum Role.";
        }
    }

    // ── ACTUALIZAR NOMBRE / APELLIDO ──────────────────────────────────────────
    @Tool(description = "Actualiza el nombre y/o apellido de un usuario buscándolo por su email")
    public String actualizarNombre(
            @ToolParam(description = "Correo electrónico del usuario a modificar") String email,
            @ToolParam(description = "Nuevo nombre (dejar vacío si no cambia)") String nuevoNombre,
            @ToolParam(description = "Nuevo apellido (dejar vacío si no cambia)") String nuevoApellido
    ) {
        User u = userRepository.findByEmail(email);
        if (u == null) return "No encontrado";
        if (nuevoNombre != null && !nuevoNombre.isBlank()) u.setName(nuevoNombre);
        if (nuevoApellido != null && !nuevoApellido.isBlank()) u.setLastName(nuevoApellido);

        userRepository.save(u);
        return "✅ Datos de " + email + " actualizados correctamente.";
    }

    // ── ACTUALIZAR NÚMERO ─────────────────────────────────────────────────────
    @Tool(description = "Actualiza el número de teléfono de un usuario buscándolo por su email")
    public String actualizarNumero(
            @ToolParam(description = "Correo electrónico del usuario") String email,
            @ToolParam(description = "Nuevo número de teléfono") String nuevoNumero
    ) {
        User u = userRepository.findByEmail(email);
        if (u == null) return "No encontrado";
        try {
            u.setNumber(new BigInteger(nuevoNumero));
            userRepository.save(u);
            return "✅ Número del usuario " + email + " actualizado a: " + nuevoNumero;
        } catch (NumberFormatException e) {
            return "❌ El número '" + nuevoNumero + "' no es válido.";
        }
    }


    // ── ELIMINAR POR EMAIL ────────────────────────────────────────────────────────
    @Tool(description = """
        Elimina un usuario del sistema usando su correo electrónico.
        Úsala cuando el usuario proporcione un email o correo para eliminar.
        """)
    public String eliminarUsuarioPorEmail(
            @ToolParam(description = "Correo electrónico del usuario a eliminar") String email
    ) {
        if (email == null || email.isBlank())
            return "❌ Debes proporcionar un correo electrónico.";

        User u = userRepository.findByEmail(email);
        if (u == null)
            return "❌ No existe ningún usuario con el correo: " + email;

        userRepository.delete(u);
        return "✅ Usuario con correo " + email + " eliminado correctamente.";
    }

    // ── ELIMINAR POR ID ───────────────────────────────────────────────────────────
    @Tool(description = """
        Elimina un usuario del sistema usando su ID numérico.
        Úsala cuando el usuario proporcione un número de id para eliminar.
        """)
    public String eliminarUsuarioPorId(
            @ToolParam(description = "ID numérico del usuario a eliminar") Long id
    ) {
        if (id == null)
            return "❌ Debes proporcionar un ID válido.";

        if (!userRepository.existsById(id))
            return "❌ No existe ningún usuario con id: " + id;

        userRepository.deleteById(id);
        return "✅ Usuario con id " + id + " eliminado correctamente.";
    }

    @Tool(description = "Crea un nuevo usuario en el sistema")
    public String crearUsuario(
            @ToolParam(description = "Nombre del usuario") String name,

            @ToolParam(description = "Username del usuario") String userName,

            @ToolParam(description = "Apellido del usuario") String lastName,

            @ToolParam(description = "Correo electrónico") String email,

            @ToolParam(description = "Contraseña") String password,

            @ToolParam(description = "Rol exacto del enum Role") String role,

            @ToolParam(description = """
                    Tipo de identificación. Usa EXACTAMENTE el nombre que existe en el sistema.
                    Si el usuario no especifica el nombre exacto, primero llama a la herramienta
                    'listarTiposIdentificacion' para obtener las opciones disponibles y luego usa una de ellas.
                    """) String identificationType,

            @ToolParam(description = "Número de identificación") BigInteger identificationNumber,

            @ToolParam(description = "Número telefónico") BigInteger number,

            @ToolParam(description = """
                    Tipo de persona. Usa EXACTAMENTE el nombre que existe en el sistema.
                    Si el usuario no especifica el nombre exacto, primero llama a la herramienta
                    'listarTiposPersona' para obtener las opciones disponibles y luego usa una de ellas.
                    """) String personType,

            @ToolParam(description = """
                    Nombre de la dependencia. Usa EXACTAMENTE el nombre que existe en el sistema.
                    Si el usuario no especifica el nombre exacto, primero llama a la herramienta
                    'listarDependencias' para obtener las opciones disponibles y luego usa una de ellas.
                    """) String dependenceName
    ) {
        List<String> faltantes = new ArrayList<>();

        if (name == null || name.isBlank()) faltantes.add("name");
        if (userName == null || userName.isBlank()) faltantes.add("userName");
        if (lastName == null || lastName.isBlank()) faltantes.add("lastName");
        if (email == null || email.isBlank()) faltantes.add("email");
        if (password == null || password.isBlank()) faltantes.add("password");
        if (role == null || role.isBlank()) faltantes.add("role");
        if (identificationType == null || identificationType.isBlank()) faltantes.add("identificationType");
        if (identificationNumber == null) faltantes.add("identificationNumber");
        if (number == null) faltantes.add("number");
        if (personType == null || personType.isBlank()) faltantes.add("personType");
        if (dependenceName == null || dependenceName.isBlank()) faltantes.add("dependenceName");

        if (!faltantes.isEmpty()) {

            return "Faltan los siguientes campos obligatorios: "
                    + String.join(", ", faltantes);
        }

        try {

            // VALIDAR ROL
            Role roleEnum;

            try {

                roleEnum = Role.valueOf(role.toUpperCase());

            } catch (Exception e) {

                return "El rol enviado no es válido. Roles permitidos: "
                        + Arrays.toString(Role.values());
            }

            if (userRepository.existsByEmail(email))
                return "Ya existe un usuario con el correo: " + email;

            if (userRepository.existsByUser(userName))
                return "Ya existe un usuario con el username: " + userName;

            if (userRepository.existsByIdentificationNumber(identificationNumber))
                return "Ya existe un usuario con la identificación: " + identificationNumber;

            if (userRepository.existsByNumber(number))
                return "Ya existe un usuario con el número: " + number;

            IdentificationType identificationTypeEntity =
                    identificationTypeRepository.findByNameIdentificationTypeIgnoreCase(identificationType);
            if (identificationTypeEntity == null)
                return "No existe el tipo de identificación: " + identificationType;

            PersonType personTypeEntity =
                    personTypeRepository.findByNamePersonTypeIgnoreCase(personType);
            if (personTypeEntity == null)
                return "No existe el tipo de persona: " + personType;

            Dependence dependence =
                    dependenceRepository.findByNameDependenceIgnoreCase(dependenceName);
            if (dependence == null)
                return "No existe la dependencia: " + dependenceName;

            // CREAR USUARIO
            User usuario = new User();

            usuario.setName(name);
            usuario.setUser(userName);
            usuario.setLastName(lastName);
            usuario.setEmail(email);

            // ENCRIPTAR PASSWORD
            usuario.setPassword(passwordEncoder.encode(password));

            usuario.setRole(roleEnum);

            // ESTADO AUTOMÁTICO
            usuario.setStateUser(new StateUser(1L, "INACTIVO"));

            usuario.setIdentificationType(identificationTypeEntity);

            usuario.setIdentificationNumber(identificationNumber);

            usuario.setNumber(number);

            usuario.setPersonType(personTypeEntity);

            usuario.setDependence(dependence);

            // GUARDAR
            userRepository.save(usuario);

            // GENERAR TOKEN
            var jwtToken = jwtService.genereteToken((UserDetails) usuario);

            // LINK ACTIVACIÓN
            String activationLink =
                    "https://pqrsmartfront.onrender.com/activate/" + jwtToken;

            // HTML CORREO
            String mensajeHtml = String.format(
                    "<h1>Hola %s %s</h1>" +
                            "<p>Gracias por registrarte en PQRSMART.</p>" +

                            "<p>Para activar tu cuenta haz clic en el siguiente enlace:</p>" +

                            "<br />" +

                            "<a href=\"%s\">Activar Cuenta</a>" +

                            "<br /><br />" +

                            "<p>Si no solicitaste este registro puedes ignorar este correo.</p>" +

                            "<br /><br />" +

                            "<p>PQRSMART</p>",

                    usuario.getName(),
                    usuario.getLastName(),
                    activationLink
            );

            // ENVIAR CORREO
            emailService.sendEmails(
                    new String[]{usuario.getEmail()},
                    "Confirma tu correo",
                    mensajeHtml
            );

            return "Usuario creado correctamente y correo enviado a: "
                    + usuario.getEmail();

        } catch (Exception e) {

            return "Error al crear el usuario: " + e.getMessage();
        }
    }

    @Tool(description = "Lista todas las dependencias disponibles en el sistema")
    public String listarDependencias() {
        return dependenceRepository.findAll()
                .stream()
                .map(Dependence::getNameDependence)
                .collect(Collectors.joining("\n"));
    }

    @Tool(description = "Lista todos los tipos de identificación disponibles en el sistema")
    public String listarTiposIdentificacion() {
        return identificationTypeRepository.findAll()
                .stream()
                .map(IdentificationType::getNameIdentificationType)
                .collect(Collectors.joining("\n"));
    }

    @Tool(description = "Lista todos los tipos de persona disponibles en el sistema")
    public String listarTiposPersona() {
        return personTypeRepository.findAll()
                .stream()
                .map(PersonType::getNamePersonType)
                .collect(Collectors.joining("\n"));
    }


}
