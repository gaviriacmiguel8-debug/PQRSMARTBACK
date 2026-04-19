package Proyecto.PQRSMART.Domain.Service.Interfaces;

import Proyecto.PQRSMART.Domain.Dto.ChatRequest;

public interface ChatService {
    String preguntarIA(String mensaje);
    String chat(ChatRequest request);
}
