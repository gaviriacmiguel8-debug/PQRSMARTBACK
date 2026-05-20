package Proyecto.PQRSMART.Domain.Service;

import Proyecto.PQRSMART.Domain.Dto.AIAnalysisResponse;
import Proyecto.PQRSMART.Persistence.Entity.Category;
import Proyecto.PQRSMART.Persistence.Entity.Dependence;
import Proyecto.PQRSMART.Persistence.Entity.Request;
import Proyecto.PQRSMART.Persistence.Entity.User;
import Proyecto.PQRSMART.Persistence.Repository.CategoryRepository;
import Proyecto.PQRSMART.Persistence.Repository.DependenceRepository;
import Proyecto.PQRSMART.Persistence.Repository.UsuarioRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MCPRequestAnalysisService {

    private final ChatClient chatClient;
    private final UsuarioRepository userRepository;

    @Autowired
    private DependenceRepository dependenceRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public AIAnalysisResponse analyze(Request request) throws JsonProcessingException {
        Dependence dependence = dependenceRepository
                .findById(request.getDependence().getIdDependence())
                .orElseThrow(() -> new RuntimeException("Dependencia no encontrada"));

        Category category = categoryRepository
                .findById(request.getCategory().getIdCategory())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));


        String prompt = """
                Analiza la siguiente PQRS.
                
                Dependencia: %s
                Categoría: %s
                Descripción: %s
                
                Debes determinar:
                
                1. Si la solicitud es coherente.
                2. Si debe rechazarse.
                3. Prioridad:
                   BAJA, MEDIA, ALTA o CRITICA.
                4. Nivel de relevancia de 0 a 1.
                
                Rechazar si:
                - la descripción no coincide con la dependencia
                - la categoría no coincide
                - es spam
                - no tiene sentido
                
                Responde SOLO en JSON usando EXACTAMENTE
                estos atributos:
                
                                             {
                                               "valid": true,
                                               "coherence": true,
                                               "relevance": 0.95,
                                               "reason": "Solicitud válida",
                                               "priority": "ALTA"
                                             }
                                             recuerda que reemplazas esos datos con los que tuyos
                
                                             NO uses español en los nombres.
                                             NO agregues texto adicional.
                                             y si se rechaza da el motivo tal cual en español
                """.formatted(
                dependence.getNameDependence(),
                category.getNameCategory(),
                request.getDescription()
        );

        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        // Convertir JSON a objeto
        ObjectMapper mapper = new ObjectMapper();

        AIAnalysisResponse ai =
                mapper.readValue(response, AIAnalysisResponse.class);

        // Buscar usuario compatible SOLO de la dependencia
        if (Boolean.TRUE.equals(ai.getValid())) {

            List<User> users =
                    userRepository.findByDependence(
                            request.getDependence()
                    );

            User assigned =
                    selectBestUser(users);

            ai.setAssignedUserId(assigned.getId());
        }

        return ai;
    }

    private User selectBestUser(List<User> users) {

        // lógica:
        // menos carga
        // activo
        // mismo rol
        // disponible

        return users.get(0);
    }
}
