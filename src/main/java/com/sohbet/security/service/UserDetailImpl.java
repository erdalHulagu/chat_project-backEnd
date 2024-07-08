package com.sohbet.security.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sohbet.domain.User;

// 
public class UserDetailImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private String email;

	private String password;

	private List<GrantedAuthority> grantedAuthorities; // kullanilacak yani authantice edilecek enum listleri

	// Burada constructor kullanarak User details icin alan methodlari burda User
	// clasini parametre vererek user detailsle implement ettik
	public UserDetailImpl(User user) {
		email = user.getEmail();
		password = user.getPassword();
		grantedAuthorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getType().name()))
				.collect(Collectors.toList());

	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {

		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}

	@Override
	public boolean isEnabled() {

		return true;
	}

}