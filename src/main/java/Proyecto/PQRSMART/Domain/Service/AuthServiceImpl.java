package Proyecto.PQRSMART.Domain.Service;

import Proyecto.PQRSMART.Config.Exception.Exceptions;
import Proyecto.PQRSMART.Controller.models.AuthResponse;
import Proyecto.PQRSMART.Controller.models.AuthenticationRequest;
import Proyecto.PQRSMART.Controller.models.RegisterRequest;
import Proyecto.PQRSMART.Domain.Service.Interfaces.AuthService;
import Proyecto.PQRSMART.Persistence.Entity.Role;
import Proyecto.PQRSMART.Persistence.Entity.StateUser;
import Proyecto.PQRSMART.Persistence.Entity.User;
import Proyecto.PQRSMART.Persistence.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UsuarioRepository userRepository;
    private final EmailServiceImpl emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUser(request.getUser())) {
            throw new Exceptions.UserAlreadyExistsException("El usuario ya existe.");
        }

        // Verificar si el email ya existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new Exceptions.EmailAlreadyExistsException("El correo electrónico ya está en uso.");
        }

        // Verificar si el número de identificación ya existe
        if (userRepository.existsByIdentificationNumber(request.getIdentificationNumber())) {
            throw new Exceptions.IdentificationNumberAlreadyExistsException("El número de identificación ya está registrado.");
        }
        // Verificar si el número de numero ya existe
        if (userRepository.existsByNumber(request.getNumber())) {
            throw new Exceptions.NumberAlreadyExistsException("El número ya está registrado.");
        }

        var user = User.builder()
                .user(request.getUser())
                .name(request.getName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .stateUser(new StateUser(1L, "INACTIVO"))
                .identificationType(request.getIdentificationType())
                .identificationNumber(request.getIdentificationNumber())
                .personType(request.getPersonType())
                .number(request.getNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .dependence(request.getDependence())
                .build();
        userRepository.save(user);

        var jwtToken = jwtService.genereteToken((UserDetails) user);
        // Enviar correo electrónico de activación
        String activationLink1 = "https://pqrsmartfront.onrender.com/activate/"+jwtToken;
        String mensajeHtml = String.format(
                "<h1>Hola %s %s</h1>" +
                        "<p>Gracias por iniciar el proceso de verificación de identidad en nuestra plataforma. Para completar la verificación, por favor haz clic en el siguiente enlace:" +
                        "<br /><br />" +
                        "<a href=\"%s\">Verificar Identidad</a>" +
                        "<br /><br />" +
                        "Este enlace te llevará a una página donde podrás confirmar tu identidad. Una vez completado este paso, tu verificación estará finalizada y podrás acceder a todos los beneficios de nuestra plataforma de manera segura." +
                        "<br /><br />" +
                        "Si tienes alguna pregunta o necesitas asistencia durante este proceso, no dudes en contactarnos respondiendo a este correo." +
                        "<br /><br />" +
                        "Gracias de nuevo por tu colaboración." +
                        "<br /><br />" +
                        "<br /><br />" +
                        "<br /><br />" +
                        "PQRSmart<br /><br />" ,
                user.getName(), user.getLastName(), activationLink1
        );

        emailService.sendEmails(
                new String[]{user.getEmail()},
                "Confirma tu correo",
                mensajeHtml
        );
        return AuthResponse.builder()
                .token(jwtToken).build();
    }

    @Override
    public AuthResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByUser(request.getUser())) {
            throw new Exceptions.UserAlreadyExistsException("El usuario ya existe.");
        }

        // Verificar si el email ya existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new Exceptions.EmailAlreadyExistsException("El correo electrónico ya está en uso.");
        }

        // Verificar si el número de identificación ya existe
        if (userRepository.existsByIdentificationNumber(request.getIdentificationNumber())) {
            throw new Exceptions.IdentificationNumberAlreadyExistsException("El número de identificación ya está registrado.");
        }
        // Verificar si el número de numero ya existe
        if (userRepository.existsByNumber(request.getNumber())) {
            throw new Exceptions.NumberAlreadyExistsException("El número ya está registrado.");
        }
        var user = User.builder()
                .user(request.getUser())
                .name(request.getName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .stateUser(new StateUser(1L, "INACTIVO"))
                .identificationType(request.getIdentificationType())
                .identificationNumber(request.getIdentificationNumber())
                .personType(request.getPersonType())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .number(request.getNumber())
                .dependence(request.getDependence())
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.genereteToken((UserDetails) user);
        // Enviar correo electrónico de activación
        String activationLink1 = "http://localhost:3000/Auth/verify-email/"+jwtToken;
        String messageHtml = String.format(
                "<!DOCTYPE html>" +
                        "<html lang='es'>" +
                        "<head>" +
                        "    <meta charset='UTF-8' />" +
                        "    <meta name='viewport' content='width=device-width, initial-scale=1.0' />" +
                        "    <style>" +
                        "        body { font-family: Arial, sans-serif; background-color: #f4f4f7; margin: 0; padding: 0; }" +
                        "        .container { max-width: 600px; margin: 40px auto; background: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 8px rgba(0,0,0,0.05); }" +
                        "        .header { background-color: #0a5ed7; padding: 20px; text-align: center; }" +
                        "        .header img { width: 120px; }" +
                        "        h1 { color: #333333; font-size: 22px; }" +
                        "        p { color: #555555; font-size: 15px; line-height: 1.6; }" +
                        "        .content { padding: 30px; text-align: left; }" +
                        "        .button { display: inline-block; margin-top: 20px; background-color: #0a5ed7; color: white; padding: 14px 24px; text-decoration: none; font-size: 16px; border-radius: 6px; }" +
                        "        .social-section { text-align: center; padding: 20px 0; }" +
                        "        .social-icons img { width: 32px; margin: 0 8px; }" +
                        "        .footer { background-color: #f0f0f0; text-align: center; padding: 20px; font-size: 13px; color: #666666; }" +
                        "        .footer a { color: #0a5ed7; text-decoration: none; }" +
                        "    </style>" +
                        "</head>" +
                        "<body>" +
                        "    <div class='container'>" +

                        "        <div class='header'>" +
                        "            <img src='https://i.ibb.co/SKDzCTc/mmo.png' alt='PQRSmart' />" +

                        "        </div>" +

                        "        <div class='content'>" +
                        "            <h1>Hola %s %s</h1>" +
                        "            <p>Gracias por iniciar tu proceso de verificación de identidad en nuestra plataforma.</p>" +
                        "            <p>Para continuar, haz clic en el siguiente botón para confirmar tu identidad:</p>" +

                        "            <a href='%s' class='button'>Verificar Identidad</a>" +

                        "            <p>Si tienes alguna duda o necesitas ayuda, puedes responder directamente a este correo.</p>" +
                        "        </div>" +

                        "        <div class='social-section'>" +
                        "            <p>Síguenos en nuestras redes sociales</p>" +
                        "            <div class='social-icons'>" +
                        "                <a href='https://facebook.com'><img src='https://cdn-icons-png.flaticon.com/512/733/733547.png' alt='Facebook'></a>" +
                        "                <a href='https://instagram.com'><img src='https://cdn-icons-png.flaticon.com/512/2111/2111463.png' alt='Instagram'></a>" +
                        "                <a href='https://linkedin.com'><img src='https://cdn-icons-png.flaticon.com/512/174/174857.png' alt='LinkedIn'></a>" +
                        "            </div>" +
                        "        </div>" +

                        "        <div class='footer'>" +
                        "            <p><strong>PQRSmart</strong></p>" +
                        "            <p>Dirección: Calle 123 #45-67, Cartagena, Colombia</p>" +
                        "            <p>Teléfono: +57 300 123 4567</p>" +
                        "            <p>Email: soporte@pqrsmart.com</p>" +
                        "            <p><a href='#'>Políticas de privacidad</a> | <a href='#'>Términos de uso</a></p>" +
                        "        </div>" +
                        "    </div>" +
                        "</body>" +
                        "</html>"
                ,
                user.getName(), user.getLastName(), activationLink1
        );

        emailService.sendEmails(
                new String[]{user.getEmail()},
                "Confirma tu correo",
                messageHtml
        );
        return AuthResponse.builder()
                .token(jwtToken).build();
    }

    @Override
    public AuthResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUser(),
                        request.getPassword()
                )
        );
        UserDetails user = userRepository.findUserByUser(request.getUser()).orElseThrow();
        String jwtToken = jwtService.genereteToken(user);
        List<String> roles = user.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());

        return AuthResponse.builder()
                .token(jwtToken)
                .authorities(roles)
                .build();
    }

    @Override
    public User getCurrentUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }



}

