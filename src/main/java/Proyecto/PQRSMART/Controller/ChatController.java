package Proyecto.PQRSMART.Controller;

import Proyecto.PQRSMART.Domain.Dto.ChatRequest;
import Proyecto.PQRSMART.Domain.Service.Interfaces.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatController {

    @Autowired
    private ChatService chatService;
    private final ChatClient chatClient;

    public ChatController(
            ChatClient.Builder chatClientBuilder,
            ToolCallbackProvider toolCallbackProvider  // ← inyectado desde UsuarioToolsConfig
    ) {
        this.chatClient = chatClientBuilder
                .defaultToolCallbacks(toolCallbackProvider) // ← registra las @Tool
                .build();
    }

    @PostMapping("/box")
    public String chat(@RequestBody ChatRequest request) {
        return chatService.chat(request);
    }


    @PostMapping("/")
    public String chat(@RequestBody String mensaje) {
        return chatService.preguntarIA(mensaje);
    }

    @GetMapping("/user")
    public String chatUser(@RequestParam("message") String message) {
        String systemPrompt = """
                Eres un asistente administrador del sistema PQRSMART.
                Gestionas la tabla de usuarios con las siguientes herramientas disponibles:
 
                - listarUsuarios        → lista todos los usuarios con su información.
                - buscarUsuarioPorEmail → busca un usuario por su correo electrónico.
                - actualizarRol        → cambia el rol de un usuario (usa el nombre exacto del enum Role).
                - actualizarNombre     → modifica el nombre y/o apellido de un usuario.
                - actualizarNumero     → actualiza el número de teléfono de un usuario.
                - eliminarUsuario      → elimina un usuario por id o por email.
                - Crear un nuevo usuario con sus datos (nombre, apellido, username, correo, contraseña, rol, tipo de identificación, número de identificación, teléfono, tipo de persona y dependencia).
                
                Analiza el mensaje del administrador, determina qué acción solicita
                y ejecuta la herramienta correspondiente.
 
                Responde siempre en español confirmando la acción realizada
                o indicando qué dato falta para poder completarla.
                Si lo que se pide no esta en las funciones decirlo que el sistema no puede procesar la peticion
                """;

        return chatClient.prompt()
                .system(systemPrompt)
                .user(message)
                .call()
                .content();
    }

}