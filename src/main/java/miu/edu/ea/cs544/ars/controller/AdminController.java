package miu.edu.ea.cs544.ars.controller;

import javassist.NotFoundException;
import miu.edu.ea.cs544.ars.domain.*;
import miu.edu.ea.cs544.ars.service.AppointmentService;
import miu.edu.ea.cs544.ars.service.PersonService;
import miu.edu.ea.cs544.ars.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final PersonService personService;
    private final SessionService sessionService;
    private final AppointmentService appointmentService;

    public AdminController(PersonService personService,
                           AppointmentService appointmentService,
                           SessionService sessionService) {
        this.personService = personService;
        this.appointmentService = appointmentService;
        this.sessionService = sessionService;
    }

    @GetMapping("/persons")
    public List<Person> persons(HttpServletRequest request) {
        return this.personService.listAll();
    }


    @RequestMapping(value = "/persons/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<RequestResponse> updatePerson(@PathVariable Integer id, @RequestBody Person person) throws NotFoundException {
        person.setId(id);
        this.personService.save(person);
        return ResponseEntity.ok(new RequestResponse("Update successful"));
    }


    @GetMapping("sessions")
    public List<Session> getSessions(@RequestParam(required = false) Integer providerId) {
        if (providerId == null)
            return sessionService.listAll();
        return sessionService.listByProviderId(providerId);

    }

    @PostMapping("sessions")
    public ResponseEntity<RequestResponse> addSession(HttpServletRequest request, @RequestBody Session session) {
        try {
            int person_id = session.getPerson().getId();
            Person person = personService.get(person_id);
            session.setPerson(person);
            sessionService.save(session);
            return ResponseEntity.ok(new RequestResponse("Session Created successful"));
        } catch (Exception e) {
            return ResponseEntity.ok(new RequestResponse("Unable to create a session."));
        }
    }

    @GetMapping("/sessions/{id}")
    public ResponseEntity<Session> getSessionById(HttpServletRequest request, @PathVariable Integer id) {
        try {
            Session session = sessionService.get(id);
            return new ResponseEntity<Session>(session, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<Session>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/sessions/{id}")
    public ResponseEntity<?> updateSession(HttpServletRequest request, @RequestBody Session session, @PathVariable Integer id) {
        try {
            Session existingSession = sessionService.get(id);
            if (existingSession != null) {
                session.setId(id);
                sessionService.update(session);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<RequestResponse> delete(@PathVariable Integer id) {
        List<Appointment> appointmments = this.appointmentService.findBySessionId(id);

        if (appointmments.isEmpty()) {
            sessionService.delete(id);
            return ResponseEntity.ok(new RequestResponse("Session deleted successfully"));
        } else {
            return ResponseEntity.ok(new RequestResponse("There are some appointments on this session.Unable to delete the session."));
        }
    }

    @GetMapping("/sessions/providers/{id}")
    public List<Session> getSessionByProviderId(HttpServletRequest request, @PathVariable Integer id) {
        return sessionService.findByProviderId(id);
    }


    //To Update any appointment -BY Hassan Raza
    @RequestMapping(value = "/appointments/{id}", method = RequestMethod.PUT)
    public boolean updateAppointment(@PathVariable int id, @RequestBody Appointment appt) throws Exception {
        appt.setId(id);
        if (appointmentService.isAppointmentExist(appt.getId())) {
            Person p = this.personService.get(appt.getPerson().getId());
            Session session = this.sessionService.get(appt.getSession().getId());
            appt.setPerson(p);
            appt.setSession(session);

            appointmentService.update(appt);
            return true;

        }
        throw new Exception("Error: Record Not Exist");
    }


    @PostMapping("appointments")
    public ResponseEntity<Object> addAppointment(@RequestBody Appointment appointment) {
        if (appointment.getPerson() == null || appointment.getPerson().getId() == null
                || appointment.getSession() == null || appointment.getSession().getId() == null) {
            return ResponseEntity.badRequest().body("Person and session id must be included in the request");
        }
        if(appointmentService.findCustomerAppointmentBySession(appointment.getPerson().getId(), appointment.getSession().getId(), appointment.getAppointmentDate())!= null)
        	 return ResponseEntity.badRequest().body("Customer already has an appointment for the specified session and date");

        if(appointment.getAppointmentDate() == null || appointment.getAppointmentDate().before(new Date())) {
        	 return ResponseEntity.badRequest().body("Appointment date must be in the future");
        }
       
        appointment.setCreationDate(new Date());
        appointment.setStatus(AppointmentStatus.CREATED);
        return ResponseEntity.ok(appointmentService.save(appointment));
    }

    //To delete any the Appointment - By Hassan Raza
    @RequestMapping(value = "/appointments/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAppointment(@PathVariable int id) {
        this.appointmentService.delete(id);
        return ResponseEntity.ok("");
    }
}


