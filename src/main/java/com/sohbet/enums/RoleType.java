package com.sohbet.enums;

public enum RoleType {
	
	ROLE_ADMIN("Administrator"),
	ROLE_ANONYMOUS("anonymous"),
	 ;
	
	private String name;
	
	// constructorı dışarı açmamak için private yapıyoruz
	private RoleType(String name) {
		this.name = name ;
	}
	
	public String getName() {
		return name ;
	}
	
	

}
