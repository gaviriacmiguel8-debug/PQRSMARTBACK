package Proyecto.PQRSMART.Controller;


import Proyecto.PQRSMART.Domain.Dto.RequestDTO;
import Proyecto.PQRSMART.Domain.Dto.RequestStateDTO;
import Proyecto.PQRSMART.Domain.Dto.UsuarioDto;
import Proyecto.PQRSMART.Domain.Mapper.RequestMapper;
import Proyecto.PQRSMART.Domain.Mapper.UsuarioMapper;
import Proyecto.PQRSMART.Domain.Service.EmailServiceImpl;
import Proyecto.PQRSMART.Domain.Service.Interfaces.PdfServices;
import Proyecto.PQRSMART.Domain.Service.Interfaces.RequestServices;
import Proyecto.PQRSMART.Domain.Service.Interfaces.RequestStateService;
import Proyecto.PQRSMART.Domain.Service.RequestServicesImpl;
import Proyecto.PQRSMART.Persistence.Entity.Request;
import Proyecto.PQRSMART.Persistence.Entity.RequestState;
import Proyecto.PQRSMART.Persistence.Entity.User;
import Proyecto.PQRSMART.Persistence.Repository.RequestRepository;
import Proyecto.PQRSMART.Persistence.Repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/request")
public class RequestController {
    @Autowired
    private RequestServices requestServices;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UsuarioRepository userRepository;
    @Autowired
    private RequestStateService requestStateService;

    @Autowired
    private PdfServices pdfServices;

    private final EmailServiceImpl emailService;

    //private final Path fileStorageLocation = Paths.get("files").toAbsolutePath().normalize();
    // Ruta para guardar archivos
    @Value("${file.upload-dir:/var/data/uploads}")
    private String uploadDir;

    @PostMapping("/save")
    public ResponseEntity<String> saveRequest(@RequestBody RequestDTO request) {
        try{

        // Obtenemos el usuario autenticado
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Buscamos el usuario en la base de datos

            User user = userRepository.findByUser(userDetails.getUsername());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario no encontrado");
        }



        // Creamos la solicitud
        request.setUser(UsuarioMapper.toDto(user));

            // Guardar solicitud usando el servicio
            RequestDTO  savedRequest = requestServices.saves(request);
            Request  requests = requestServices.findEntityByIds(request);
            Optional<Request> searchRequest = requestServices.findEntityById(savedRequest.getIdRequest());

            Request requestAll;
            String requestJson;
                if (searchRequest.isPresent()) {
                    // Convertir la solicitud guardada a JSON y devolverla
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.registerModule(new JavaTimeModule());
                    requestAll = searchRequest.get();
                    requestAll.setUser(user);
                    requestAll.setRequestType(requests.getRequestType());
                    requestAll.setCategory(requests.getCategory());
                    requestAll.setDependence(requests.getDependence());
                    requestAll.setRequestState(requests.getRequestState());
                    System.out.println("empieza " + requestAll);
                    requestJson = mapper.writeValueAsString(requestAll);



                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Solicitud no encontrada");
                }

            byte[] pdf = pdfServices.generarPdfSolicitud(user, requestAll);

                System.out.println("archivo url: "+ request.getArchivo());

            emailService.sendEmailWithPdf(
                    user.getEmail(),
                    "Detalle de Solicitud",
                    "Adjunto encontrarás el PDF con los detalles.",
                    pdf, request.getArchivo()

            );


            // 7️⃣ Devolver objeto directamente (Spring lo convierte a JSON)
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(requestJson);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + e.getMessage());
        }
    }

    @GetMapping("/get")
    public List<RequestDTO> get() {

        return requestServices.getAll();
    }

    @GetMapping("/getForDependence")
    public List<RequestDTO> getForDependence() {

        return requestServices.getForDependence();
    }

    @GetMapping("/get/pqrs")
    public List<RequestDTO> getPqrs() {
        // Obtenemos el usuario autenticado
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return requestServices.getPqrs(userDetails.getUsername());
    }


    @PutMapping("/cancel/{id}")
    public ResponseEntity<RequestDTO> cancelarSolicitud(@PathVariable Long id) {
        RequestDTO requestCancelado = requestServices.cancelar(id);

        return ResponseEntity.ok(requestCancelado);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RequestDTO requestDTO ) throws IOException {
        Optional<RequestDTO> requestDTOOptional = requestServices.findById(id);
        if (requestDTOOptional.isPresent()) {
            RequestDTO existingRequest = requestDTOOptional.get();
            existingRequest.setAnswer(requestDTO.getAnswer());


            // Actualizar otros campos si es necesario
            RequestDTO updatedRequestDTO = requestServices.save(existingRequest); // Guardar los cambios en la solicitud existente
            return ResponseEntity.ok(updatedRequestDTO);
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/report/{year}/{month}")
    public ResponseEntity<byte[]> generateReport(
            @PathVariable int year,
            @PathVariable int month) throws Exception {

        List<Request> pqrs = requestServices.getByMonth(year, month);

        byte[] pdf = requestServices.generateReport(pqrs, year);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename("reporte_pqrs.pdf")
                        .build()
        );

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}