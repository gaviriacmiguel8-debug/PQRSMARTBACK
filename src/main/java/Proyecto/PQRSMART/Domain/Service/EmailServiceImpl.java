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
import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sibApi.TransactionalEmailsApi;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailAttachment;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${supabase.bucket.public.url}")
    private String supabasePublicUrl;

    @Value("${brevo.api.key}")
    private String brevoApiKey;
/*
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

            throw new RuntimeException("❌ Error enviando correo: " + e.getMessage(), e);
            // Manejar la excepción apropiadamente según tu aplicación
        }
    }*/


    @Override
    public void sendEmails(String[] toUser, String subject, String message) {
        try {
            ApiClient client = Configuration.getDefaultApiClient();
            client.setApiKey(brevoApiKey);

            TransactionalEmailsApi api = new TransactionalEmailsApi();

            // Sender
            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail("gaviriacmiguel.8@gmail.com");
            sender.setName("pqrsmart");

            // Destinatarios
            List<SendSmtpEmailTo> recipients = Arrays.stream(toUser)
                    .map(addr -> {
                        SendSmtpEmailTo to = new SendSmtpEmailTo();
                        to.setEmail(addr);
                        return to;
                    })
                    .collect(Collectors.toList());

            // Email
            SendSmtpEmail email = new SendSmtpEmail();
            email.setSender(sender);   // 👈 setSender, no setFrom
            email.setTo(recipients);
            email.setSubject(subject);
            email.setHtmlContent(message);

            api.sendTransacEmail(email);
            System.out.println("✅ Correo enviado");

        } catch (Exception e) {
            throw new RuntimeException("❌ Error enviando correo: " + e.getMessage(), e);
        }
    }

    public void sendEmailWithPdf(String to, String subject, String body, byte[] pdfData, String archivoRuta) {
        try {
            ApiClient client = Configuration.getDefaultApiClient();
            client.setApiKey(brevoApiKey);

            TransactionalEmailsApi api = new TransactionalEmailsApi();

            // Sender
            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail("gaviriacmiguel.8@gmail.com");
            sender.setName("pqrsmart");

            SendSmtpEmailTo recipient = new SendSmtpEmailTo();
            recipient.setEmail(to);

            // PDF generado
            SendSmtpEmailAttachment pdfAttachment = new SendSmtpEmailAttachment();
            pdfAttachment.setName("solicitud.pdf");
            pdfAttachment.setContent(pdfData);

            List<SendSmtpEmailAttachment> attachments = new ArrayList<>();
            attachments.add(pdfAttachment);

            // Archivo de Supabase
            if (archivoRuta != null && !archivoRuta.isEmpty()) {
                byte[] archivoBytes = descargarArchivo(archivoRuta);
                String fileName = archivoRuta.substring(archivoRuta.lastIndexOf("/") + 1)
                        .replace("%20", " ");

                SendSmtpEmailAttachment archivoAttachment = new SendSmtpEmailAttachment();
                archivoAttachment.setName(fileName);
                archivoAttachment.setContent(archivoBytes);
                attachments.add(archivoAttachment);
            }

            SendSmtpEmail email = new SendSmtpEmail();
            email.setSender(sender);
            email.setTo(List.of(recipient));
            email.setSubject(subject);
            email.setHtmlContent(body);
            email.setAttachment(attachments);

            api.sendTransacEmail(email);
            System.out.println("✅ Correo con PDF enviado");

        } catch (Exception e) {
            throw new RuntimeException("Error al enviar correo: " + e.getMessage());
        }
    }

  /*  public void sendEmailWithPdf(String to, String subject, String body, byte[] pdfData, String archivoRuta) {
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
    }*/
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
