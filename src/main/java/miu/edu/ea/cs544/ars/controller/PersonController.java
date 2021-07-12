package miu.edu.ea.cs544.ars.controller;

import javassist.NotFoundException;
import miu.edu.ea.cs544.ars.domain.Person;
import miu.edu.ea.cs544.ars.domain.RequestResponse;
import miu.edu.ea.cs544.ars.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/persons")
    public List<Person> persons(HttpServletRequest request) {
        return this.personService.listAll();
    }

    @RequestMapping(value = "persons", method = RequestMethod.POST)
    public ResponseEntity<RequestResponse> savePerson(@RequestBody Person person) {
        this.personService.save(person);
        return ResponseEntity.ok(new RequestResponse("Post Successful"));
    }

    @RequestMapping(value = "persons/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<RequestResponse> updatePerson(@PathVariable Integer id, @RequestBody Person person)
            throws NotFoundException {
        person.setId(id);
        this.personService.save(person);
        return ResponseEntity.ok(new RequestResponse("Update successful"));
    }

}
