package miu.edu.ea.cs544.ars.Runner;

import miu.edu.ea.cs544.ars.domain.*;
import miu.edu.ea.cs544.ars.domain.enums.RoleType;
import miu.edu.ea.cs544.ars.repository.PersonRepositoryInterface;
import miu.edu.ea.cs544.ars.repository.RoleRepository;
import miu.edu.ea.cs544.ars.repository.SessionRepositoryInterface;
import miu.edu.ea.cs544.ars.service.AppointmentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
public class StartupRunner implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final PersonRepositoryInterface personRepositoryInterface;
    private final SessionRepositoryInterface sessionRepositoryInterface;
    private final AppointmentService appointmentService;

    public StartupRunner(RoleRepository roleRepository,
                         PersonRepositoryInterface personRepositoryInterface,
                         SessionRepositoryInterface sessionRepositoryInterface,
                         AppointmentService appointmentService) {
        this.roleRepository = roleRepository;
        this.personRepositoryInterface = personRepositoryInterface;
        this.sessionRepositoryInterface = sessionRepositoryInterface;
        this.appointmentService = appointmentService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Role adminRole = new Role(RoleType.ADMIN.toString());
        this.roleRepository.save(adminRole);
        Role customerRole = new Role(RoleType.CUSTOMER.toString());
        this.roleRepository.save(customerRole);
        Role counselorRole = new Role(RoleType.COUNSELOR.toString());
        this.roleRepository.save(counselorRole);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Person admin = new Person("admin", passwordEncoder.encode("admin"));
        admin.setFirstName("admin");
        admin.setLastName("K");
        admin.setDob(new Date());
        admin.setEmail("karsten.rivas@gmail.com");
        admin.addRole(adminRole);
        Person customer = new Person("customer", passwordEncoder.encode("customer"));
        customer.setFirstName("customer");
        customer.setLastName("P");
        customer.setDob(new Date());
        customer.setEmail("karsten.rivas@gmail.com");
        customer.addRole(customerRole);
        Person counselor = new Person("provider", passwordEncoder.encode("provider"));
        counselor.setFirstName("provider");
        counselor.setLastName("M");
        counselor.setDob(new Date());
        counselor.setEmail("krstnrivas@gmail.com");
        counselor.addRole(counselorRole);
        this.personRepositoryInterface.save(admin);
        this.personRepositoryInterface.save(customer);
        this.personRepositoryInterface.save(counselor);


        Session session = new Session("fairfield", 120, "2021-10-10", 1000);
        session.setPerson(counselor);
        this.sessionRepositoryInterface.save(session);

        Appointment appointment = new Appointment(new Date(), new Date(), customer, session, AppointmentStatus.CREATED);
        this.appointmentService.save(appointment);
    }
}