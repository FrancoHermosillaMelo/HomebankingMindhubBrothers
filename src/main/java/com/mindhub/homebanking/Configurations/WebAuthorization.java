package com.mindhub.homebanking.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

            http.authorizeRequests()
                    .antMatchers("/rest/**").denyAll()
                    .antMatchers("/web/index.html").permitAll()
                    .antMatchers("/web/js/index.js").permitAll()
                    .antMatchers("/web/css/**").permitAll()
                    .antMatchers("/web/img/**").permitAll()
                    .antMatchers(HttpMethod.POST,"/api/clients").permitAll()
                    .antMatchers(HttpMethod.POST,"/api/login").permitAll()
                    .antMatchers(HttpMethod.POST,"/api/logout").permitAll()
                    .antMatchers("/manager.html").hasAuthority("ADMIN")
                    .antMatchers("/h2-console/**").hasAuthority("ADMIN")
                    .antMatchers("/api/clients","/api/clients/").hasAuthority("ADMIN")
                    .antMatchers("/web/js/**").hasAnyAuthority("CLIENT", "ADMIN")
                    .antMatchers("/web/html/**").hasAnyAuthority("CLIENT", "ADMIN")
                    .antMatchers("/api/clients/current").hasAnyAuthority("CLIENT", "ADMIN")
                    .antMatchers("/api/clients/current/cards").hasAnyAuthority("CLIENT", "ADMIN")
                    .antMatchers("/api/clients/current/accounts/{id}").hasAnyAuthority("CLIENT", "ADMIN")
                    .antMatchers("/api/clients/current/accounts").hasAnyAuthority("CLIENT", "ADMIN")
                    .antMatchers("/api/loans").hasAnyAuthority("CLIENT", "ADMIN")
                    .antMatchers(HttpMethod.POST, "/api/clients/current/transaction").hasAnyAuthority("CLIENT", "ADMIN")
                    .antMatchers(HttpMethod.POST,"/api/clients/current/cards").hasAnyAuthority("CLIENT", "ADMIN")
                    .antMatchers(HttpMethod.POST,"/api/clients/current/accounts").hasAnyAuthority("CLIENT", "ADMIN")
                    .antMatchers(HttpMethod.POST,"/api/loans").hasAnyAuthority("CLIENT", "ADMIN")
                    .antMatchers(HttpMethod.PUT,"/api/clients/current/cards/{id}").hasAnyAuthority("CLIENT", "ADMIN")
                    .antMatchers(HttpMethod.PUT,"/api/accounts/{id}").hasAnyAuthority("CLIENT", "ADMIN")
                    .anyRequest().denyAll();

            http.formLogin()

                    .usernameParameter("email")

                    .passwordParameter("password")

                    .loginPage("/api/login");

            http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");

            http.csrf().disable();

            //deshabilitar frameOptions para que se pueda acceder a h2-console

            http.headers().frameOptions().disable();

            // si el usuario no está autenticado, simplemente envíe una respuesta de falla de autenticación

            http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

            // si el inicio de sesión es exitoso, simplemente borre las alertas que solicitan autenticación

            http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

            // si el inicio de sesión falla, simplemente envíe una respuesta de falla de autenticación

            http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

            // si el cierre de sesión es exitoso, simplemente envíe una respuesta exitosa

            http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());


        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }

    }
}
