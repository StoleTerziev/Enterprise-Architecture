package miu.edu.ea.cs544.ars.repository;

import miu.edu.ea.cs544.ars.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface PersonRepositoryInterface extends JpaRepository<Person, Integer> {
    @Query(value = "SELECT p FROM persons p WHERE p.username = ?1")
    Person findPersonByUsername(String username);

    @Override
    @Query("select NEW persons(s.firstName, s.lastName, s.email, s.dob, s.username) from persons s")
    List<Person> findAll();

    @Query("select distinct NEW persons(s.id, s.firstName, s.lastName, s.email, s.dob, s.username) " +
            "from persons s join s.roles r WHERE r.name = ?1")
    List<Person> findAllByRole(String role);
}
