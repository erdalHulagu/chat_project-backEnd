package com.sohbet.security.jwt;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

public class AuthTokenFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {
	    String jwtToken = parseJwt(request);
	    
	    logger.info("Extracted JWT Token: {}", jwtToken);  // Add this line to log the token.
	    
	    try {
	        if(jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {
	            String email = jwtUtils.getEmailFromToken(jwtToken);
	            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
	            
	            UsernamePasswordAuthenticationToken authenticationToken = new 
	                    UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	            
	            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	        }
	    } catch (Exception e) {
	        logger.error("User not Found: {}", e.getMessage());
	    }
	    
	    filterChain.doFilter(request, response);
	}

	private String parseJwt(HttpServletRequest request) {
	    String header = request.getHeader("Authorization");
	    if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
	        String token = header.substring(7);
	        // Check if the token contains 2 periods (valid JWT format)
	        if (token.chars().filter(ch -> ch == '.').count() == 2) {
	            return token;
	        }
	        logger.error("Invalid JWT format: {}", token);
	    }
	    return null;
	}

	
	
	// filtrelenmemesini istediğim end-pointler
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		AntPathMatcher antPathMatcher = new AntPathMatcher();
		return antPathMatcher.match("/register", request.getServletPath()) || 
				      antPathMatcher.match("/login",request.getServletPath());
	}

}
