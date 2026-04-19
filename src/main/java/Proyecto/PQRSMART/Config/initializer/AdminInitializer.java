package Proyecto.PQRSMART.Config.initializer;


import Proyecto.PQRSMART.Persistence.Entity.Role;
import Proyecto.PQRSMART.Persistence.Entity.StateUser;
import Proyecto.PQRSMART.Persistence.Entity.User;
import Proyecto.PQRSMART.Persistence.Repository.DependenceRepository;
import Proyecto.PQRSMART.Persistence.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UsuarioRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DependenceRepository dependenciaRepository;

    @Override
    public void run(String... args) {
        var dependencia = dependenciaRepository.findById(7L)
                .orElseThrow(() -> new RuntimeException("Dependencia 7 no existe"));

        if (!userRepository.existsByUser("admin")) {

            var admin = User.builder()
                    .user("admin")
                    .name("Administrador")
                    .lastName("Sistema")
                    .email("admin@pqrsmart.com")
                    .password(passwordEncoder.encode("Admin123*"))
                    .role(Role.ADMIN)
                    .dependence(dependencia)
                    .stateUser(new StateUser(2L, "ACTIVO"))

                    .build();

            userRepository.save(admin);
        }
    }
}