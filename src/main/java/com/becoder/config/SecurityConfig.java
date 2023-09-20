package com.becoder.config;

import com.becoder.config.oauth.CustomOAuth2UserService;
import com.becoder.config.oauth.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    public CustomAuthSucessHandler sucessHandler;
    @Autowired
    private CustomOAuth2UserService oAuth2UserService;
    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandle;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService getDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(getDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

   @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
   {
       http.csrf().disable()
                .authorizeHttpRequests().requestMatchers("/","/register","/signin","/createUser","/oauth/**","/verify","/forgot_password","/reset_password").permitAll()
                .requestMatchers("/user/**","/admin/**").authenticated().and()
                .formLogin().loginPage("/signin")
                .usernameParameter("username").passwordParameter("password")
               .successHandler(sucessHandler).permitAll().and()
               .oauth2Login()
               .loginPage("/signin")
               .userInfoEndpoint()
               .userService(oAuth2UserService)
               .and().successHandler(oAuth2LoginSuccessHandle).permitAll();
//        http.csrf().disable()
//                .authorizeHttpRequests().requestMatchers("/user/**").hasRole("USER")
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//                .requestMatchers("/staff/**").hasRole("STAFF")
//                .requestMatchers("/**").permitAll().and().formLogin().loginPage("/signin").loginProcessingUrl("/userLogin")
//                .successHandler(sucessHandler).permitAll().and()
//                .oauth2Login().loginPage("/signin")
//                .userInfoEndpoint().userService(oAuth2UserService)
//               .and().successHandler(oAuth2LoginSuccessHandle);



        return http.build();
   }


}
