package Proyecto.PQRSMART.Domain.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;

import Proyecto.PQRSMART.Domain.Dto.ChatRequest;
import Proyecto.PQRSMART.Domain.Service.Interfaces.ChatService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.reactive.function.client.WebClient;
import org.stringtemplate.v4.ST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service

public class ChatServiceImpl implements ChatService {


//    private final ChatClient chatClient;

    @Value("${groq.api-key}")
    private String apiKey;
    public String preguntarIA(String mensaje) {
        RestTemplate restTemplate = new RestTemplate();
        String prompt = """
                Eres un asistente experto en PQRS en Colombia.
                Redacta documentos formales, claros y jurídicos.
                No inventes datos.
                
                Puedes:
                - Explicar qué es cada tipo de PQRS
                - Redactar quejas, reclamos, peticiones y sugerencias
                - Pedir información si falta
                - Responder de forma clara y formal
                Estructura:
                - Encabezado
                - Hechos
                - Solicitud
                - Despedida
                Responde al usuario de forma útil.
                
                y si te piden algo diferente al estos temas rechazas
                
                y no pidas informacion personal nada de nombre, identificacion ni nada de eso ya que en la peticion ya va incluida
                
                Usuario: """ + mensaje;

return "";
//        return chatClient.prompt()
//                .user(prompt)
//                .call()
//                .content();

    }


//    public String chat(ChatRequest request) {
//        try {
//            List<String> conversation = new ArrayList<>();
//
//            for (var msg : request.getMessages()) {
//                conversation.add(msg.getRole() + ": " + msg.getContent());
//            }
//
//            String fullPrompt = """
//                    Eres un asistente experto en PQRS en Colombia.
//
//                    Funciones:
//                    - Redactar quejas, reclamos, peticiones y sugerencias
//                    - Explicar conceptos
//                    - Mejorar textos
//
//                    Reglas:
//                    - Responder SIEMPRE de forma formal
//                    - No inventar información
//                    - Si falta información, solicitarla
//
//                    Estructura obligatoria:
//                    - Encabezado
//                    - Hechos
//                    - Solicitud
//                    - Despedida
//
//                    y no pidas informacion personal nada de nombre, identificacion ni nada de eso ya que en la peticion ya va incluida
//
//                    Si la solicitud no es de PQRS, indícalo claramente.
//                    """ + String.join("\n", conversation) + "\nRespuesta:";
//
//            return chatClient.prompt()
//                    .user(fullPrompt)
//                    .call()
//                    .content();
//        } catch (Exception e) {
//            return e.toString();
//        }
//    }



    public String chat(ChatRequest request) {
        try {

            WebClient webClient = WebClient.builder()
                    .baseUrl("https://api.groq.com/openai/v1")
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .build();

            List<Map<String, String>> messages = new ArrayList<>();

            // 🔥 TU PROMPT AQUÍ (system)
            messages.add(Map.of(
                    "role", "system",
                    "content", """
                        Eres un asistente experto en PQRS en Colombia.

                        Funciones:
                        - Redactar quejas, reclamos, peticiones y sugerencias
                        - Explicar conceptos
                        - Mejorar textos

                        Reglas:
                        - Responder SIEMPRE de forma formal
                        - No inventar información
                        - Si falta información, solicitarla
                        - NO pedir datos personales

                        Estructura obligatoria:
                        - Encabezado
                        - Hechos
                        - Solicitud
                        - Despedida

                        Si la solicitud no es de PQRS, indícalo claramente.
                        """
            ));

            // 👇 Conversación del usuario
            for (var msg : request.getMessages()) {
                messages.add(Map.of(
                        "role", msg.getRole(),
                        "content", truncate(msg.getContent())
                ));
            }
            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> body = new HashMap<>();
            body.put("model", "openai/gpt-oss-120b");
            body.put("messages", messages);

            String response = webClient.post()
                    .uri("/chat/completions")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
// 👇 EXTRAER SOLO EL TEXTO
            JsonNode root = mapper.readTree(response);
            String content = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();
            return content;

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    private String truncate(String text) {
        int max = 1000; // caracteres
        return text.length() > max ? text.substring(0, max) : text;
    }

}
