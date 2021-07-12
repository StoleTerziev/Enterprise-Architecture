package miu.edu.ea.cs544.ars.controller;

import miu.edu.ea.cs544.ars.domain.Appointment;
import miu.edu.ea.cs544.ars.domain.Person;
import miu.edu.ea.cs544.ars.domain.RequestResponse;
import miu.edu.ea.cs544.ars.domain.Session;
import miu.edu.ea.cs544.ars.service.AppointmentService;
import miu.edu.ea.cs544.ars.service.PersonService;
import miu.edu.ea.cs544.ars.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/providers")
public class ProviderController {

    private final SessionService sessionservice;
    private final PersonService personservice;
    private final AppointmentService appointmentService;

    public ProviderController(SessionService sessionservice,
                              PersonService personservice,
                              AppointmentService appointmentService) {
        this.sessionservice = sessionservice;
        this.personservice = personservice;
        this.appointmentService = appointmentService;
    }


    @GetMapping("/sessions")
    public List<Session> list(HttpServletRequest request) {
        int userid = (int) request.getAttribute("user_id");
        return sessionservice.findByProviderId(userid);

    }

    @PostMapping("sessions")
    public ResponseEntity<RequestResponse> add(HttpServletRequest request, @RequestBody Session session) {
        try {
            int userid = (int) request.getAttribute("user_id");
            Person person = personservice.get(userid);
            session.setPerson(person);
            sessionservice.save(session);
            return ResponseEntity.ok(new RequestResponse("Session Created successful"));
        } catch (Exception e) {
            return ResponseEntity.ok(new RequestResponse("Unable to create a session."));
        }
    }

    @GetMapping("/sessions/{id}")
    public ResponseEntity<Session> get(HttpServletRequest request, @PathVariable Integer id) {
        try {
            int userid = (int) request.getAttribute("user_id");
            Session session = sessionservice.findByProviderIdAndSessionID(id, userid);
            return new ResponseEntity<Session>(session, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<Session>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/sessions/{id}")
    public ResponseEntity<RequestResponse> update(HttpServletRequest request, @RequestBody Session session, @PathVariable Integer id) {
        try {
            int userid = (int) request.getAttribute("user_id");
            Session existingSession = sessionservice.findByProviderIdAndSessionID(id, userid);
            if (existingSession != null) {
                session.setId(id);
                session.setPerson(existingSession.getPerson());
                sessionservice.update(session);
                return ResponseEntity.ok(new RequestResponse("Session updated successful"));
            } else {
                return ResponseEntity.ok(new RequestResponse("Unable to update a session."));
            }

        } catch (NoSuchElementException e) {
            return ResponseEntity.ok(new RequestResponse("Unable to update a session."));
        }
    }

    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<RequestResponse> delete(HttpServletRequest request, @PathVariable Integer id) {
        int userid = (int) request.getAttribute("user_id");
        List<Appointment> appointmments = this.appointmentService.findBySessionIDAndProviderId(id, userid);

        if (appointmments.isEmpty()) {
            sessionservice.delete(id);
            return ResponseEntity.ok(new RequestResponse("Session deleted successfully"));
        } else {
            return ResponseEntity.ok(new RequestResponse("There are some appointments on this session.Unable to delete the session."));
        }
    }

    @GetMapping("/appointments")
    public List<Appointment> getListAppointment(HttpServletRequest request) {
        Integer providerId = Integer.valueOf(request.getAttribute("user_id").toString());
        return this.appointmentService.getAppointmentByProviderId(providerId);
    }

    @PutMapping("/appointments/{id}")
    public ResponseEntity<RequestResponse> approveAppointment(HttpServletRequest request, @PathVariable Integer id) throws Exception {
        Integer providerId = Integer.valueOf(request.getAttribute("user_id").toString());
        this.appointmentService.approveById(id, providerId);
        return ResponseEntity.ok(new RequestResponse("Approved"));
    }

}
