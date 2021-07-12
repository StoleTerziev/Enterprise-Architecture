package miu.edu.ea.cs544.ars.security;

import miu.edu.ea.cs544.ars.domain.Person;
import miu.edu.ea.cs544.ars.repository.PersonRepositoryInterface;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final PersonRepositoryInterface personRepo;

    JwtUserDetailsService(PersonRepositoryInterface personRepo) {
        this.personRepo = personRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //demo 3 roles
        if ("admi1n".equals(username)) {
            // load user from database
            List<GrantedAuthority> authorities = new ArrayList<>();
            // add role admin
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            return new User("admin", passwordEncoder.encode("admin"), authorities);
        } else {
            //get from database
            Person person = this.personRepo.findPersonByUsername(username);
            if (person == null) throw new UsernameNotFoundException("User not found with username: " + username);
            return new User(person.getUsername(), person.getPassword(), person.getAuthorities());
        }
    }
}