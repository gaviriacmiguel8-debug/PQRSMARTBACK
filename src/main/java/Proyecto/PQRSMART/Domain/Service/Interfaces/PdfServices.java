package Proyecto.PQRSMART.Domain.Service.Interfaces;

import Proyecto.PQRSMART.Domain.Dto.RequestDTO;
import Proyecto.PQRSMART.Domain.Dto.UsuarioDto;
import Proyecto.PQRSMART.Persistence.Entity.Request;
import Proyecto.PQRSMART.Persistence.Entity.User;

import java.util.List;

public interface PdfServices {
     byte[] generarPdfSolicitud(User user, Request request);

    byte[] generateReport(List<Request> pqrsDelMes, int anio);
    byte[] generateReportUser(List<UsuarioDto> users);
}
