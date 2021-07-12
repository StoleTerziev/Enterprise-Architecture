package miu.edu.ea.cs544.ars.controller;

import miu.edu.ea.cs544.ars.domain.JwtRequest;
import miu.edu.ea.cs544.ars.domain.JwtResponse;
import miu.edu.ea.cs544.ars.domain.Person;
import miu.edu.ea.cs544.ars.repository.PersonRepositoryInterface;
import miu.edu.ea.cs544.ars.security.JwtTokenUtil;
import miu.edu.ea.cs544.ars.security.JwtUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthenticateController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;
    private final PersonRepositoryInterface personRepository;

    AuthenticateController(AuthenticationManager authenticationManager,
                           JwtTokenUtil jwtTokenUtil,
                           JwtUserDetailsService userDetailsService,
                           PersonRepositoryInterface personRepository1) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.personRepository = personRepository1;
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> authenticate(@RequestBody JwtRequest request) throws Exception {
        // check authenticate if valid user
        authenticate(request.getUsername(), request.getPassword());

        // get user from data by username
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        // generate token
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @GetMapping("/person")
    public Optional<Person> getPerson() {
        return this.personRepository.findById(1);
    }

    private void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}

