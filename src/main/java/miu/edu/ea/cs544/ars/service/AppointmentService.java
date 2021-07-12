package miu.edu.ea.cs544.ars.service;


import miu.edu.ea.cs544.ars.domain.Appointment;
import miu.edu.ea.cs544.ars.domain.AppointmentStatus;
import miu.edu.ea.cs544.ars.domain.Person;
import miu.edu.ea.cs544.ars.domain.Session;
import miu.edu.ea.cs544.ars.repository.AppointmentRepositoryInterface;
import miu.edu.ea.cs544.ars.repository.PersonRepositoryInterface;
import miu.edu.ea.cs544.ars.repository.SessionRepositoryInterface;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepositoryInterface appointmentrepo;
    private final PersonRepositoryInterface personrepo;
    private final SessionRepositoryInterface sessionrepo;

    public AppointmentService(AppointmentRepositoryInterface appointmentrepo,
                              PersonRepositoryInterface personrepo,
                              SessionRepositoryInterface sessionrepo) {
        this.appointmentrepo = appointmentrepo;
        this.personrepo = personrepo;
        this.sessionrepo = sessionrepo;
    }

    public List<Appointment> listAll() {
        // filter by person_id
        return appointmentrepo.findAll();
    }

    public List<Appointment> getAppointmentByPersonId(Integer personId) {
        return this.appointmentrepo.findAllAppointmentByPersonId(personId);
    }

    public List<Appointment> getAppointmentByProviderId(Integer providerId) {
        return this.appointmentrepo.findAllAppointmentByProviderId(providerId);
    }
    
    public Appointment findCustomerAppointmentBySession(Integer customerId, Integer sessionId, Date date) {
        return this.appointmentrepo.findCustomerAppointmentBySession(customerId, sessionId, date).stream().findFirst().orElse(null);
    }

    public Appointment save(Appointment appointment) {
        Person customer = personrepo.findById(appointment.getPerson().getId()).get();
        Session session = sessionrepo.findById(appointment.getSession().getId()).get();
        appointment.setPerson(customer);
        appointment.setSession(session);
        return appointmentrepo.save(appointment);
    }

    public Appointment get(Integer id) {
        return appointmentrepo.findById(id).get();
    }

    public void delete(Integer id) {

        if (this.appointmentrepo.existsById(id))
            this.appointmentrepo.deleteById(id);
    }

    public boolean cancel(Integer id) {
        Appointment appointment = this.appointmentrepo.getOne(id);

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = appointment.getSession().getStartTime();
            d2 = new Date();

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();
            long diffHours = diff / (60 * 60 * 1000) % 24;
            if (diffHours < 48) {
                return false;
            }
        } catch (Exception ignored) {
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
        this.appointmentrepo.save(appointment);
        return true;
    }

    public void approveById(Integer id, Integer providerId) throws Exception {
        Appointment appointment = this.appointmentrepo.getOne(id);
        if (!appointment.getSession().getPerson().getId().equals(providerId)) {
            throw new Exception("Can't approve this appointment");
        }
        appointment.setStatus(AppointmentStatus.APPROVED);
        this.appointmentrepo.save(appointment);
    }

    //Check appointment exist in the DB by ID
    public boolean isAppointmentExist(int id) {
        return this.appointmentrepo.existsById(id);
    }

    //Check Session By ID
    public boolean CheckSessionExist(int id) {
        return appointmentrepo.existsById(id);
    }

    //Check Person By ID
    public boolean CheckPersonExist(int id) {
        return personrepo.existsById(id);
    }

    //To Update any appointment
    public Appointment update(Appointment appointment) {
        // Delete the Previous appointment

        // this.appointmentRepo.deleteById(appointment.getId());
        //add the appointment in The DB
        return this.appointmentrepo.save(appointment);

    }

    public List<Appointment> findBySessionId(Integer id) {
        return appointmentrepo.findBySessionId(id);
    }

    public List<Appointment> findBySessionIDAndProviderId(Integer id, Integer providerId) {
        return appointmentrepo.findBySessionIDAndProviderId(id, providerId);
    }
}
