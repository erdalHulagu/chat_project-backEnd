package com.sohbet.security.config;

    

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sohbet.security.jwt.JwtAuthFilter;
import com.sohbet.security.service.UserDetailServiceImpl;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // bu method ben metod bazli calisacam diyor
public class SecurityConfig {
	
	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	
	@Bean
	public UserDetailsService userDetailsService() {
		
		
		return new UserDetailServiceImpl();
	}
	

	// bu methoddada authentike etmek istedigimz end pointleri hallediyoruz
	 
@SuppressWarnings("deprecation")
protected void configure(HttpSecurity http) throws Exception {
	        http.csrf().disable()
	                .authorizeRequests()
	                .requestMatchers("/"
	                		,"chats/single"
	                         ,"images/**"
	                         ,"users/**"
	                         ,"/login"
	                         ,"/register/**"
	                         ,"/js"
	                         ,"/css").permitAll()
	                .anyRequest().authenticated()
	                .and()
	                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	                .and()
	                .authenticationProvider(authProvider())
	                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
	    }

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        return http.build();
	    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
	}
	
	@Bean
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
		
		daoAuthenticationProvider.setUserDetailsService(userDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		
		return authenticationConfiguration.getAuthenticationManager();
		
	}
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
	    
//	    //*******************SWAGGER***********************
	    
	    private static final String [] AUTH_WHITE_LIST= {
				"/v3/api-docs/**", // swagger
				"swagger-ui.html", //swagger
				"/swagger-ui/**", // swagger
				"/",
				"index.html",
				"/images/**",
				"/css/**",
				"/js/**"
		};

		// yukardaki static listeyi de giriş izni veriyoruz, boiler plate
	    @Bean
		public WebSecurityCustomizer webSecurityCustomizer() {
			WebSecurityCustomizer customizer=new WebSecurityCustomizer() {
				@Override
				public void customize(WebSecurity web) {
					web.ignoring().requestMatchers(AUTH_WHITE_LIST);
				}
			};
			return customizer;
		}

}
	
	
	



    

	
	
	
	


