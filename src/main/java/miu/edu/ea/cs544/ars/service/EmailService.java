package miu.edu.ea.cs544.ars.service;

import miu.edu.ea.cs544.ars.domain.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {
	@Autowired
	private JavaMailSender emailSender;

	@Value("${spring.mail.username}")
	private String fromEmail;




	public void sendCustomerAppointmentNotification(Appointment appointment, boolean isNew) {		
		String content = getTemplate(appointment, isNew, true);
		SendEmail(appointment.getPerson().getEmail(), "Appointment confirmation",content );

	}

	public void sendProviderAppointmentNotification(Appointment appointment, boolean isNew) {
		String content = getTemplate(appointment, isNew, false);
		SendEmail(appointment.getSession().getPerson().getEmail(), "Appointment confirmation",content );

	}
	
	
	private String getTemplate(Appointment appointment, boolean isNew, boolean isCustomer) {
		String label = isNew ? "created":"updated";
		StringBuilder str = new StringBuilder();
		str.append("Hi " + (isCustomer ? appointment.getPerson().getFirstName() : appointment.getSession().getPerson().getFirstName())+"\n");
		str.append("The appointment #" + appointment.getId() + " has been "+ label+"\n");
		str.append("Details \n");
		str.append("Date: " + appointment.getAppointmentDateText() +"\n");
		str.append("Location: " + appointment.getSession().getLocation() +"\n");
		if(isCustomer) {
			str.append("Provider: " + appointment.getSession().getPerson().getFullName() +"\n");
			str.append("Email: " + appointment.getSession().getPerson().getEmail() +"\n");
		}else {
			str.append("Customer: " + appointment.getPerson().getFullName() +"\n");
			str.append("Email: " + appointment.getPerson().getEmail() +"\n");
		}
		return str.toString();
	}
	
	
	private void SendEmail(String to, String subject, String content) {
		SimpleMailMessage message = new SimpleMailMessage(); 
		message.setFrom(fromEmail);
		message.setTo(to); 
		message.setSubject(subject); 
		message.setText(content);
		emailSender.send(message);
	}

}
