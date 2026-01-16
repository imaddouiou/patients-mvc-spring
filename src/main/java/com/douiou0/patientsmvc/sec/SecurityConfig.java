package com.douiou0.patientsmvc.sec;

import com.douiou0.patientsmvc.sec.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;


@Configuration //lire au premier lieu
@EnableWebSecurity //active security web
public class SecurityConfig extends WebSecurityConfigurerAdapter  {
    @Autowired
    private DataSource dataSource;//data source current
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder ;
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        PasswordEncoder passwordEncoder = passwordEncoder(); supp apres dep du bean vers app
       /* String encodedPassword = passwordEncoder.encode("1234");
        System.out.println(encodedPassword);
        //comment trouve l utulisateur pour l acces
        auth.inMemoryAuthentication()
                .withUser("user1").password(encodedPassword).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder.encode("1111")).roles("USER","ADMIN");
        auth.inMemoryAuthentication()
                .withUser("user2").password(passwordEncoder.encode("2345")).roles("USER");
        //l utulisateur qui ont le droit*/
//        auth avec jdbc mysql ,je pense remplir manuellement en base de donnees
        /*
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username as principal , password as credentials, active from users where username=?")
                .authoritiesByUsernameQuery("Select username as principal,role as role from users_roles where username=?")
                .rolePrefix("ROLE_")
                .passwordEncoder(passwordEncoder);//BCrypt

         */

        auth.userDetailsService(userDetailsService);//

    }

    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin();
//        partage les permissions
        http.authorizeRequests()
                // 🔓 ressources statiques
                .antMatchers(
                        "/webjars/**",
                        "/css/**",
                        "/js/**",
                        "/images/**"
                ).permitAll();
        http.authorizeRequests().antMatchers("/").permitAll();
//        http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN");avant UserDetailsService
        http.authorizeRequests().antMatchers("/admin/**").hasAuthority("ADMIN");
//        http.authorizeRequests().antMatchers("/user/**").hasRole("USER");
        http.authorizeRequests().antMatchers("/user/**").hasAuthority("USER");
        /*http.authorizeRequests().antMatchers("/delete/**","/edit/**","/save/**","/formPatients/**").hasRole("ADMIN");
        http.authorizeRequests().antMatchers("/index/**").hasRole("USER");*/
        http.authorizeRequests().anyRequest().authenticated();
        // oblige toute requête non encore autorisée à nécessiter une authentification
        http.exceptionHandling().accessDeniedPage("/403");
    }

/*  on n achanger l emplacement c mieu etre en application
    @Bean
    PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder();
    }
*/

}
