package pl.lodz.pl.it.cardio.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity //- opcjonalna, bo nie wyłączyłem security config
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private  MyUserDetailsService myUserDetailsService;
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    public SecurityConfiguration(MyUserDetailsService myUserDetailsService, AccessDeniedHandler accessDeniedHandler) {
        this.myUserDetailsService = myUserDetailsService;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new MySimpleUrlAuthenticationSuccessHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().httpBasic().and()
                .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .antMatchers("/register", "/registrationConfirm*", "/badUser.html" ,"/h2-console").permitAll()
                .antMatchers("/**").hasAnyRole("CLIENT","MECHANIC")
            .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                //.passwordParameter("password")
                //.loginProcessingUrl("/employee")
                .failureForwardUrl("/login-error")
                .successHandler(myAuthenticationSuccessHandler())
                .permitAll()
            .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/index")
                .permitAll();

        http.headers().frameOptions().disable();

        /*http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/home", "/about").permitAll()
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);*/


        //http.authorizeRequests()
        //        .antMatchers("/css/**", "/js/**", "/img/**"
        //        , "/*.js", "/*.css", "/*.img", "/layouts/**", "/static/**").permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
