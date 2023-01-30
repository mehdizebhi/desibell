package ir.desibell.notificationService.config.security;

import ir.desibell.notificationService.jwt.filter.JwtRequestFilter;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JwtRequestFilter JwtRequestFilter;

    public SecurityConfig(DataSource dataSource, JwtRequestFilter JwtRequestFilter) {
        this.dataSource = dataSource;
        this.JwtRequestFilter = JwtRequestFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery("select number,password,enabled from user where number = ?")
                .authoritiesByUsernameQuery("select number,role from authorities where number = ?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/", "/index", "/sub", "/css/**", "/js/**",
                        "/assets/**", "/fonts/**", "/login", "/signup", "/auth",
                        "/forgetPassword/**", "/purchase/return/**" , "/get-start/**")
                .permitAll()
                .and().authorizeRequests()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").usernameParameter("number")
                .and().logout().logoutUrl("/logout").deleteCookies("remember_me", "access_token", "crt").permitAll()
                .and().exceptionHandling().accessDeniedPage("/access-denied")
                .and().rememberMe().rememberMeCookieName("remember_me").tokenValiditySeconds(86400).rememberMeParameter("remember_me")
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(JwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
