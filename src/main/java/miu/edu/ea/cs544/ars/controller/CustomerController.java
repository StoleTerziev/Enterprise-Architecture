package miu.edu.ea.cs544.ars.controller;

import javassist.NotFoundException;
import miu.edu.ea.cs544.ars.domain.Appointment;
import miu.edu.ea.cs544.ars.domain.Person;
import miu.edu.ea.cs544.ars.domain.RequestResponse;
import miu.edu.ea.cs544.ars.domain.Session;
import miu.edu.ea.cs544.ars.repository.PersonRepositoryInterface;
import miu.edu.ea.cs544.ars.service.AppointmentService;
import miu.edu.ea.cs544.ars.service.PersonService;
import miu.edu.ea.cs544.ars.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final AppointmentService appointServiceObj;
    private final PersonService personService;
    private final SessionService sessionService;
    private final PersonRepositoryInterface personRepositoryInterface;

    public CustomerController(AppointmentService appointServiceObj,
                              PersonRepositoryInterface personRepositoryInterface,
                              PersonService personService,
                              SessionService sessionService) {
        this.appointServiceObj = appointServiceObj;
        this.personRepositoryInterface = personRepositoryInterface;
        this.personService = personService;
        this.sessionService = sessionService;
    }

    @RequestMapping(value = "/appointments/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<RequestResponse> deleteAppointment(HttpServletRequest request, @PathVariable int id) {
        Integer userId = Integer.valueOf(request.getAttribute("user_id").toString());

        Appointment appointment = this.appointServiceObj.get(id);
        if (!appointment.getPerson().getId().equals(userId)) {
            return ResponseEntity.badRequest().body(new RequestResponse("You dont have permission to delete this appointment"));
        }
        this.appointServiceObj.delete(id);
        return ResponseEntity.ok(new RequestResponse("Delete Successful"));
    }

    @GetMapping("/appointments")
    public List<Appointment> getAll(HttpServletRequest request) {
        Integer userId = Integer.valueOf(request.getAttribute("user_id").toString());
        return this.appointServiceObj.getAppointmentByPersonId(userId);
    }

    @RequestMapping(value = "/appointments", method = RequestMethod.POST)
    public Appointment addAppointment(@RequestBody Appointment appointment) throws Exception {

        if (appointServiceObj.CheckSessionExist(appointment.getSession().getId())
                && appointServiceObj.CheckPersonExist(appointment.getPerson().getId())) {
            Person person = this.personService.get(appointment.getPerson().getId());
            Session session = this.sessionService.get(appointment.getSession().getId());
            appointment.setPerson(person);
            appointment.setSession(session);

            appointServiceObj.save(appointment);

            return appointment;
        }
        throw new Exception("Error: Session ID or Person Id is incorrect");
    }


    @RequestMapping(value = "/appointments/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<RequestResponse> updateAppointment(@PathVariable Integer id, @RequestBody Appointment appointment) throws NotFoundException {
        appointment.setId(id);
        this.appointServiceObj.save(appointment);
        return ResponseEntity.ok(new RequestResponse("Update successful"));
    }


    @RequestMapping(value = "/appointments/{id}/cancel", method = RequestMethod.PUT)
    public ResponseEntity<RequestResponse> cancelAppointment(@PathVariable Integer id) throws NotFoundException {
        if (!this.appointServiceObj.cancel(id)) {
            return ResponseEntity.badRequest().body(new RequestResponse("Can't cancel the appointment, please contact the admin!"));
        }
        return ResponseEntity.ok(new RequestResponse("Update successful"));
    }


    @GetMapping("/providers")
    public List<Person> getAllProviders() {
        return this.personService.findAllProvider();
    }
}
