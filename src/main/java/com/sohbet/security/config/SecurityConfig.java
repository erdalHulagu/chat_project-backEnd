package com.sohbet.security.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sohbet.security.jwt.AuthTokenFilter;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableTransactionManagement
public class SecurityConfig {
	
	@Autowired
	private UserDetailsService userDetailsService;
//	
//	 @Bean
//	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//	        return http.csrf(AbstractHttpConfigurer::disable)
//	                .authorizeHttpRequests(auth->{
//	                    auth.requestMatchers("/"
//		                         ,"images/**"
//		                         ,"users/**"
//		                         ,"login"
//		                         ,"/register/**"
//		                         ,"/js"
//		                         ,"/css").permitAll()
//	                            .anyRequest().authenticated();
//	                })
//	                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//	                .authenticationProvider(authProvider())
//	                .addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class)
//	                .build();
//	    }
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.csrf(csrf -> csrf.disable())
	        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
	            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	            .requestMatchers(
	                "/login",
	                "/register",
	                "/files/download/**",
	                "/contactmessage/visitors",
	                "/files/display/**",
	                "/car/visitors/**",
	                "/actuator/info",
	                "/actuator/health"
	            ).permitAll()
	            .anyRequest().authenticated()
	        );
	    
	    http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	    return http.build();
	}
    //*************** cors Ayarları ****************************
    
    @Bean
   	public WebMvcConfigurer corsConfigurer() {
   		return new WebMvcConfigurer() {
   			@Override
   			public void addCorsMappings(CorsRegistry registry) {
   				registry.addMapping("/**")
   				        .allowedOrigins("*") //"http:127.0.0.1/8080 diye spesific adresden gelenleri kabul et de diyebiliriz
   						.allowedHeaders("*")
   						.allowedMethods("*");
   			}
   		};
   	}
    
//    //*******************SWAGGER***********************
//    
//    private static final String [] AUTH_WHITE_LIST= {
//			"/v3/api-docs/**", // swagger
//			"swagger-ui.html", //swagger
//			"/swagger-ui/**", // swagger
//			"/",
//			"index.html",
//			"/images/**",
//			"/css/**",
//			"/js/**"
//	};
//
//	// yukardaki static listeyi de giriş izni veriyoruz, boiler plate
//    @Bean
//	public WebSecurityCustomizer webSecurityCustomizer() {
//		WebSecurityCustomizer customizer=new WebSecurityCustomizer() {
//			@Override
//			public void customize(WebSecurity web) {
//				web.ignoring().requestMatchers(AUTH_WHITE_LIST);
//			}
//		};
//		return customizer;
//	}

    

    
    
    @Bean
    public AuthTokenFilter authTokenFilter() {
    	return new AuthTokenFilter();
    }
    
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder(10);
    }
    
    @Bean
    public AuthenticationProvider  authProvider() {
    	DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    	authenticationProvider.setUserDetailsService(userDetailsService);
    	authenticationProvider.setPasswordEncoder(passwordEncoder());
    	
    	return authenticationProvider;
    	
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    }
    
    

	
	
	
	


