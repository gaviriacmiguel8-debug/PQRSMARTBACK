package Proyecto.PQRSMART.Persistence.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Solicitudes ")
public class Request {

    @Column(name = "ID_Solicitud")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRequest;

    @Column(name = "Radicado", unique = true)
    private String radicado;

    @JoinColumn(name = "Usuario")
    @ManyToOne
    private User user;

    @JoinColumn(name = "ID_Tipo_Solicitud")
    @ManyToOne(fetch = FetchType.EAGER)
    private RequestType requestType;

    @JoinColumn(name = "ID_Categoria")
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    
    @Column(name = "Descripcion_Solicitud", columnDefinition = "TEXT")
    private String description;


    @Column(name = "Fecha_Solicitud")
    private LocalDate date;

    @Column(name = "Respuesta_Solicitud")
    private String answer;

    @JoinColumn(name = "Id_Estado_Solicitud")
    @ManyToOne(fetch = FetchType.EAGER)
    private RequestState requestState;

    @Column(name = "Medio_Respuesta")
    private String mediumAnswer;

    @JoinColumn (name = "ID_Dependencia")
    @ManyToOne
    private Dependence dependence;


    @Column(name = "Archivo")
    private String archivo;


    @Column(name = "Archivo_Respuesta")
    private String archiveAnswer;
    @Enumerated(EnumType.STRING)

    @Column(name = "Prioridad")
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "Usuario_Asignado")
    private User assignedUser;

    @Column(name = "Motivo_Rechazo")
    private String rejectReason;

    @Column(name = "Coherencia_IA")
    private Boolean aiCoherence;

    @Column(name = "Relevancia")
    private Double relevance;

}
