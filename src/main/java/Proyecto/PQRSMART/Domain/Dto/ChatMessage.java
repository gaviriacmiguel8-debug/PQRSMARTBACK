package Proyecto.PQRSMART.Domain.Dto;

import lombok.Data;

@Data
public class ChatMessage {
    private String role; // "user" o "assistant"
    private String content;

    // getters y setters
}