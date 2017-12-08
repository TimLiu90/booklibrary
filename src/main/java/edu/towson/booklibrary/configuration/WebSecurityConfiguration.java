package edu.towson.booklibrary.configuration;

import edu.towson.booklibrary.handler.AccessDeniedHandler;
import edu.towson.booklibrary.handler.FailureLoginHandler;
import edu.towson.booklibrary.handler.SuccessLoginHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

/**
 * Created by lastcow_chen on 7/7/17.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Autowired
    FailureLoginHandler failureLoginHandler;
    @Autowired
    SuccessLoginHandler successLoginHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http
                .authorizeRequests()
                    .antMatchers("/pages/**").permitAll()
                    .antMatchers("/css/**").permitAll()
                    .antMatchers("/images/**").permitAll()
                    .antMatchers("/video/**").permitAll()
                    .antMatchers("/assets/**").permitAll()
                    .antMatchers("/about").permitAll()
                    .antMatchers("/registration").permitAll()
                    .antMatchers("/register_user").permitAll()
                    .antMatchers("/dashboard/admin/**").hasAnyRole("ADMIN")
                    .antMatchers("/api/public/**").permitAll()
                    .antMatchers("/api/parent/**").hasAnyRole("PARENT")
                    .antMatchers("/api/admin/**").hasAnyRole("ADMIN")
                    .antMatchers("/action/admin/**").hasAnyRole("ADMIN")
                    .antMatchers("/student/new").hasAnyRole("PARENT")
                    .antMatchers("/admin/**").hasAnyRole("ADMIN", "BORROWER")
                .antMatchers("/").hasAnyRole("ADMIN", "BORROWER")
                    .antMatchers("/api/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/dashboard", false)
//                    .successHandler(successLoginHandler)
                    .failureHandler(failureLoginHandler)
                    .permitAll()
                    .and()
                .logout()
                    .permitAll()
                    .and()
                .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler);
    }
}
