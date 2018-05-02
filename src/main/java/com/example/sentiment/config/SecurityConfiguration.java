package com.example.sentiment.config;


import com.example.sentiment.services.SentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    //@Autowired
    PasswordEncoder passwordEncoder =
            PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Autowired
    private SentUserService sentUserService;


//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//                //URLs for Spring Security to ignore
//                .antMatchers("searchForTweets/**");
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http.requiresChannel().anyRequest().requiresSecure();

        http
                .headers()
                .frameOptions()
                .sameOrigin();

        http.csrf().disable().authorizeRequests()
                .antMatchers("/resources/**", "/registration/**", "/login/**", "/static/**").permitAll()
                .antMatchers("/searchForTweets/**").hasRole("USER")
                .antMatchers("/premium/**").hasRole("USER")
                .and()
            .formLogin()
                .loginPage("/login").defaultSuccessUrl("/premium")
                  .permitAll()
                .and()
            .logout().logoutSuccessUrl("/index")
                .permitAll();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(sentUserService);
        auth.setPasswordEncoder(passwordEncoder);
        return auth;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(authenticationProvider());
    }


}

