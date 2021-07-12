package miu.edu.ea.cs544.ars.service;

import miu.edu.ea.cs544.ars.domain.Person;
import miu.edu.ea.cs544.ars.domain.enums.RoleType;
import miu.edu.ea.cs544.ars.repository.PersonRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    @Autowired
    private final PersonRepositoryInterface personRepository;

    public PersonService(PersonRepositoryInterface personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> listAll() {
        return personRepository.findAll();
    }

    public List<Person> findAllProvider() {
        return personRepository.findAllByRole(RoleType.COUNSELOR.toString());
    }

    public Person get(Integer id) {
        return personRepository.findById(id).get();
    }

    public Person findPersonByUsername(String username) {
        return personRepository.findPersonByUsername(username);
    }

    public void save(Person person) {
        personRepository.save(person);
    }


    public void delete(Integer id) {
        personRepository.deleteById(id);
    }

}