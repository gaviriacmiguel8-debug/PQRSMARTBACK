package Proyecto.PQRSMART.Domain.Service;


import Proyecto.PQRSMART.Domain.Dto.RequestDTO;
import Proyecto.PQRSMART.Domain.Mapper.RequestMapper;
import Proyecto.PQRSMART.Domain.Service.Interfaces.PdfServices;
import Proyecto.PQRSMART.Domain.Service.Interfaces.RequestServices;
import Proyecto.PQRSMART.Persistence.Entity.*;
import Proyecto.PQRSMART.Persistence.Repository.*;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestServicesImpl implements RequestServices {
    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private RequestStateRepository requestStateRepository;

    @Autowired
    private RequestTypeRepository requestTypeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DependenceRepository dependenceRepository;

    @Autowired
    private PdfServices pdfServices;



    public List<RequestDTO> getAll() {

        return requestRepository.findAll().stream().map(RequestMapper::toDTO).collect(Collectors.toList());
    }
    public List<RequestDTO> getForDependence() {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();

            // Buscar usuario en BD
            User user = userRepository.findByUser(userDetails.getUsername());

            // Obtener dependencia
            Dependence dependence = user.getDependence();

            // Filtrar por dependencia
            return requestRepository.findByDependence(dependence)
                    .stream()
                    .map(RequestMapper::toDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las solicitudes: " + e.getMessage());
        }
    }
    @Transactional(readOnly = true)
    public List<RequestDTO> getPqrs(String usurious) {

        return requestRepository.findByUserUser(usurious)
                .stream()
                .map(RequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<RequestDTO> findById(Long id) {
        return requestRepository.findById(id).map(RequestMapper::toDTO);
    }

    public Optional<Request> findEntityById(Long id) {
        return requestRepository.findById(id);
    }

    public Request findEntityByIds(RequestDTO dto) {



        RequestType requestType = requestTypeRepository
                .findById(dto.getRequestType().getIdRequestType())
                .orElseThrow(() -> new RuntimeException("Tipo de solicitud no encontrado"));

        Category category = categoryRepository
                .findById(dto.getCategory().getIdCategory())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Dependence dependence = dependenceRepository
                .findById(dto.getDependence().getIdDependence())
                .orElseThrow(() -> new RuntimeException("Dependencia no encontrada"));

        User user = userRepository
                .findById(dto.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        RequestState requestState = requestStateRepository
                .findById(dto.getRequestState().getIdRequestState())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        return Request.builder()


                .requestType(requestType)
                .category(category)
                .dependence(dependence)

                .requestState(requestState)

                .build();
    }
    public void update(RequestDTO requestDTO) {
        Request request = RequestMapper.toEntity(requestDTO);
        requestRepository.save(request);
    }

    public RequestDTO save(RequestDTO requestDTO) {
        Optional<Request> existingRequestOptional = requestRepository.findById(requestDTO.getIdRequest());
        if (existingRequestOptional.isPresent()) {
            Request existingRequest = existingRequestOptional.get();
            // Actualizar los campos relevantes de la solicitud existente con los valores de requestDTO
            RequestState state = requestStateRepository
                    .findByNameRequestState("Finalizado")
                    .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

            existingRequest.setRequestState(state);

            existingRequest.setAnswer(requestDTO.getAnswer());
            if (requestDTO.getArchiveAnswer() != null && !requestDTO.getArchiveAnswer().isEmpty()) {
                existingRequest.setArchiveAnswer(requestDTO.getArchiveAnswer());
            }
            // Actualizar otros campos si es necesario
            requestRepository.save(existingRequest);
            return requestDTO;
        } else {
            // Si la solicitud no existe, guardar una nueva solicitud en la base de datos
            requestRepository.save(RequestMapper.toEntity(requestDTO));
            return requestDTO;
        }
    }

    public RequestDTO saves(RequestDTO request) {
        // Primero, guarda la solicitud sin el radicado para que se genere el ID automáticamente.
        Request savedRequest = requestRepository.save(RequestMapper.toEntity(request));
        // 2. Obtener el ID categoría (ajusta si tu modelo cambia)
        Long dependenceId = savedRequest.getDependence().getIdDependence();

        // 3. Generar radicado
        String radicado = generarRadicadoSecuencial(savedRequest.getIdRequest(),dependenceId );
        // Ahora que el ID ya está generado, puedes asignarlo al campo 'radicado'.
        savedRequest.setRadicado(radicado); // Asumiendo que getIdRequest() devuelve el ID

        // Guarda nuevamente para actualizar el radicado.
        requestRepository.save(savedRequest);

        return RequestMapper.toDTO(savedRequest);
    }


    public RequestDTO cancelar(Long id) {

        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No encontrado"));


        RequestState canceladoState =
                requestStateRepository.findByNameRequestState("Cancelado")
                        .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        request.setRequestState(canceladoState);

        Request saved = requestRepository.save(request);

        return RequestMapper.toDTO(saved);
    }
    public byte[] generateReport(List<Request> pqrsDelMes, int anio) {

        return pdfServices.generateReport(pqrsDelMes, anio);
    }
    public List<Request> getByMonth(int year, int month) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        return requestRepository.findByDateBetween(startDate, endDate);
    }
    private String letraPorTipoSolicitud(Long requestTypeId) {
        return switch (requestTypeId.intValue()) {
            case 1 -> "P"; // Petición
            case 2 -> "Q"; // Queja
            case 3 -> "R"; // Reclamo
            case 4 -> "S"; // Sugerencia
            default -> "X"; // Por si llega algo diferente
        };
    }

    public String generarRadicadoSecuencial(Long id, Long requestTypeId) {

        long numero = id + 1; // SUMAR 1 AL ID
        String letra = letraPorTipoSolicitud(requestTypeId);

        return "PQRS-" + String.format("%05d", numero) + letra;
    }
}