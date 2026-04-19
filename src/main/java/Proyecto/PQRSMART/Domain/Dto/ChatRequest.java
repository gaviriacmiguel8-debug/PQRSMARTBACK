package Proyecto.PQRSMART.Domain.Dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ChatRequest {
    private List<ChatMessage> messages;

}