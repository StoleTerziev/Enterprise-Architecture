package miu.edu.ea.cs544.ars.security;

import miu.edu.ea.cs544.ars.domain.enums.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableAspectJAutoProxy
@EnableAsync
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService jwtUserDetailsService;
    private final FilterRequest filterRequest;

    public WebSecurityConfig(UserDetailsService jwtUserDetailsService,
                             FilterRequest filterRequest) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.filterRequest = filterRequest;
    }

    @Autowired
    protected void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(encoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/auth/token").permitAll().
                antMatchers("/auth/person").hasAnyAuthority(RoleType.ADMIN.toString())
                .antMatchers("admin/**").hasAnyAuthority(RoleType.ADMIN.toString())
                .antMatchers("customer/**").hasAnyAuthority(RoleType.CUSTOMER.toString())
                .antMatchers("provider/**").hasAnyAuthority(RoleType.COUNSELOR.toString())
                .anyRequest().authenticated().and().
                exceptionHandling().authenticationEntryPoint(null).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        http.addFilterBefore(filterRequest, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
