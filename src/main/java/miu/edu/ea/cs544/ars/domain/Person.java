package miu.edu.ea.cs544.ars.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.*;

@Data
@NoArgsConstructor
@Entity(name = "persons")
public class Person {


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "person_roles",
            joinColumns = {@JoinColumn(name = "person_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    @JsonIgnore
    protected Set<Role> roles = new HashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    @Temporal(TemporalType.DATE)
    @JsonIgnore
    private Date dob;
    @JsonIgnore
    private String username;
    @JsonIgnore
    private String password;

    public Person(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Person(String firstName, String lastName, String email, Date dob, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = dob;
        this.username = username;
    }

    public Person(Integer id, String firstName, String lastName, String email, Date dob, String username) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = dob;
        this.username = username;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities
                = new ArrayList<>();
        roles.stream()
                .map(p -> new SimpleGrantedAuthority(p.getName()))
                .forEach(authorities::add);

        return authorities;
    }


    public String getFullName() {
        return firstName + " " + lastName;
    }


}
