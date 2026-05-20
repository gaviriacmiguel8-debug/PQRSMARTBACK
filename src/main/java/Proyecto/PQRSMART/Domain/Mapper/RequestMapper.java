package Proyecto.PQRSMART.Domain.Mapper;

import Proyecto.PQRSMART.Domain.Dto.*;
import Proyecto.PQRSMART.Persistence.Entity.*;

public class RequestMapper {

    public static Request toEntity(RequestDTO dto) {

        if (dto == null) return null;

        Request request = new Request();

        request.setIdRequest(dto.getIdRequest());
        request.setRadicado(dto.getRadicado());
        request.setDescription(dto.getDescription());
        request.setDate(dto.getDate());
        request.setAnswer(dto.getAnswer());
        request.setMediumAnswer(dto.getMediumAnswer());
        request.setArchivo(dto.getArchivo());
        request.setArchiveAnswer(dto.getArchiveAnswer());
        request.setAiCoherence(dto.getAiCoherence());
        request.setPriority(dto.getPriority());
        request.setRelevance(dto.getRelevance());
        request.setRejectReason(dto.getRejectReason());

        // 🔥 Relaciones SOLO con ID (sin traer toda la entidad)

        if (dto.getUser() != null) {
            User user = new User();
            user.setId(dto.getUser().getId());
            request.setUser(user);
        }
        if (dto.getAssignedUser() != null) {
            User user = new User();
            user.setId(dto.getAssignedUser().getId());
            request.setAssignedUser(user);
        }

        if (dto.getRequestType() != null) {
            RequestType type = new RequestType();
            type.setIdRequestType(dto.getRequestType().getIdRequestType());
            request.setRequestType(type);
        }

        if (dto.getCategory() != null) {
            Category category = new Category();
            category.setIdCategory(dto.getCategory().getIdCategory());
            request.setCategory(category);
        }

        if (dto.getDependence() != null) {
            Dependence dependence = new Dependence();
            dependence.setIdDependence(dto.getDependence().getIdDependence());
            request.setDependence(dependence);
        }

        if (dto.getRequestState() != null) {
            RequestState state = new RequestState();
            state.setIdRequestState(dto.getRequestState().getIdRequestState());
            request.setRequestState(state);
        }

        return request;
    }

    public static RequestDTO toDTO(Request request) {

        if (request == null) return null;

        RequestDTO dto = new RequestDTO();

        dto.setIdRequest(request.getIdRequest());
        dto.setRadicado(request.getRadicado());
        dto.setDescription(request.getDescription());
        dto.setDate(request.getDate());
        dto.setAnswer(request.getAnswer());
        dto.setMediumAnswer(request.getMediumAnswer());
        dto.setArchivo(request.getArchivo());
        dto.setArchiveAnswer(request.getArchiveAnswer());
        dto.setAiCoherence(request.getAiCoherence());
        dto.setPriority(request.getPriority());
        dto.setRelevance(request.getRelevance());
        dto.setRejectReason(request.getRejectReason());


        // 🔥 Convertimos Entity → DTO correctamente

        if (request.getUser() != null) {
            dto.setUser(
                    UsuarioDto.builder()
                            .id(request.getUser().getId())
                            .user(request.getUser().getUsername())
                            .email(request.getUser().getEmail())
                            .build()
            );
        }
        if (request.getAssignedUser() != null) {
            dto.setAssignedUser(
                    UsuarioDto.builder()
                            .id(request.getAssignedUser().getId())
                            .user(request.getAssignedUser().getUsername())
                            .email(request.getAssignedUser().getEmail())
                            .build()
            );
        }

        if (request.getRequestState() != null) {
            dto.setRequestState(
                    RequestStateDTO.builder()
                            .idRequestState(request.getRequestState().getIdRequestState())
                            .nameRequestState(request.getRequestState().getNameRequestState())
                            .build()
            );
        }
        if (request.getDependence() != null) {
            dto.setDependence(
                    DependenceDTO.builder()
                            .idDependence(request.getDependence().getIdDependence())
                            .nameDependence(request.getDependence().getNameDependence())
                            .build()
            );
        }
        if (request.getCategory() != null) {
            dto.setCategory(
                    CategoryDTO.builder()
                            .idCategory(request.getCategory().getIdCategory())
                            .nameCategory(request.getCategory().getNameCategory())
                            .build()
            );
        }
        if (request.getRequestType() != null) {
            dto.setRequestType(
                    RequestTypeDTO.builder()
                            .idRequestType(request.getRequestType().getIdRequestType())
                            .nameRequestType(request.getRequestType().getNameRequestType())
                            .build()
            );
        }
        // Puedes hacer lo mismo para Category, Dependence y RequestType

        return dto;
    }
}