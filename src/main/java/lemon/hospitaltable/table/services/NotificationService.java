package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.objects.Doctor;
import lemon.hospitaltable.table.objects.Treatment;
import lemon.hospitaltable.table.repositories.DoctorsRepositoryInterface;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationService {
    private final JavaMailSender mailSender;
    private final DoctorsRepositoryInterface doctorsRepository;

    public void sendEmail(Treatment treatment, String subject, String text) {

        Doctor doctor = doctorsRepository.findById(treatment.doctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with ID: " + treatment.doctorId()));

        SimpleMailMessage message = new SimpleMailMessage();

        String doctorEmail = doctor.email();
        if (doctorEmail == null || doctorEmail.isEmpty()) {
            message.setTo("ok.net.set@gmail.com");
            message.setSubject(subject + " But aim doctor doesn't have e-mail address.");
            message.setText(
                    "Attention! Doctor " + doctor.name() + " ID " + doctor.id() + " doesn't have e-mail address. \n" +
                    "Message from that e-mail: \n" + text);
        } else {
            message.setTo(doctorEmail);
            message.setSubject(subject);
            message.setText(text);
        }
        message.setFrom("ok.net.set@gmail.com");
        mailSender.send(message);
    }
}
