package Proyecto.PQRSMART.Domain.Service;

import Proyecto.PQRSMART.Domain.Service.Interfaces.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Service
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${supabase.bucket.public.url}")
    private String supabasePublicUrl;

    @Override
    public void sendEmails(String[] toUser, String subject, String message) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("miguelgaviriam.8@gmail.com");
            helper.setTo(toUser);
            helper.setSubject(subject);
            helper.setText(message, true);  // Aquí se especifica que el contenido es HTML
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Manejar la excepción apropiadamente según tu aplicación
        }
    }
    public void sendEmailWithPdf(String to, String subject, String body, byte[] pdfData, String archivoRuta) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("miguelgaviriam.8@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);

            // PDF generado
            helper.addAttachment("solicitud.pdf", new ByteArrayResource(pdfData));

            // Archivo de Supabase
            if (archivoRuta != null && !archivoRuta.isEmpty()) {
                byte[] archivoBytes = descargarArchivo(archivoRuta);

                // Nombre original del archivo
                String fileName = archivoRuta.substring(archivoRuta.lastIndexOf("/") + 1)
                        .replace("%20", " ");

                helper.addAttachment(fileName, new ByteArrayResource(archivoBytes));
            }

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar correo: " + e.getMessage());
        }
    }
    public byte[] descargarArchivo(String url) {
        try {

            // 1️⃣ Extraer la parte después del último /
            String fileKey = url.substring(url.lastIndexOf("/") + 1);

            // 2️⃣ Convertir %20 → espacio normal (Supabase usa espacio real)
            fileKey = fileKey.replace("%20", " ");

            // 3️⃣ Usar EXACTAMENTE esa key dentro del bucket
            String endpoint = supabasePublicUrl
                    + fileKey;

            System.out.println("🔍 KEY EN SUPABASE: " + fileKey);
            System.out.println("🔍 URL FINAL: " + endpoint);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = restTemplate.getForEntity(endpoint, byte[].class);

            return response.getBody();

        } catch (Exception ex) {
            throw new RuntimeException("Error descargando archivo: " + ex.getMessage());
        }
    }
}
