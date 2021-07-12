package miu.edu.ea.cs544.ars.advice;

import miu.edu.ea.cs544.ars.domain.Appointment;
import miu.edu.ea.cs544.ars.service.EmailService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class NotificationAdvice {
    private final EmailService emailService;

    public NotificationAdvice(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @AfterReturning(value = "execution(* miu.edu.ea.cs544.ars.service.AppointmentService.save(..))", returning = "appointment")
    public void afterReturningAdviceSaveAppointment(JoinPoint joinPoint, Appointment appointment) {
        emailService.sendCustomerAppointmentNotification(appointment, true);
        emailService.sendProviderAppointmentNotification(appointment, true);
    }

    @Async
    @AfterReturning(value = "execution(* miu.edu.ea.cs544.ars.service.AppointmentService.update(..))", returning = "appointment")
    public void afterReturningAdviceUpdateAppointment(JoinPoint joinPoint, Appointment appointment) {
        emailService.sendCustomerAppointmentNotification(appointment, false);
        emailService.sendProviderAppointmentNotification(appointment, false);
    }
}
