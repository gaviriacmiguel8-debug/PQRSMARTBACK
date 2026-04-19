package Proyecto.PQRSMART.Controller;

import Proyecto.PQRSMART.Domain.Dto.ChatRequest;
import Proyecto.PQRSMART.Domain.Service.Interfaces.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/box")
    public String chat(@RequestBody ChatRequest request) {
        return chatService.chat(request);
    }


    @PostMapping("/")
    public String chat(@RequestBody String mensaje) {
        return chatService.preguntarIA(mensaje);
    }
}